package uk.mushow.safetynet.repository;

import org.springframework.stereotype.Repository;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.exceptions.PersonNotFoundException;
import uk.mushow.safetynet.model.Person;

@Repository
public class PersonRepository {

    private final DataWrapper dataWrapper;

    public PersonRepository(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public Person create(Person person) {
        dataWrapper.persons().add(person);
        return person;
    }

    public Person update(Person updatedPerson) {
        String id = updatedPerson.getFirstName() + updatedPerson.getLastName();
        for (Person currentPerson : dataWrapper.persons()) {
            if ((currentPerson.getFirstName() + currentPerson.getLastName()).equals(id) ) {
                updatePersonInformation(currentPerson, updatedPerson);
                return currentPerson;
            }
        }
        throw new PersonNotFoundException("The person: " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName() + "was not found!");
    }

    public void delete(String firstName, String lastName) {
        boolean wasRemoved = dataWrapper.persons().removeIf(person ->
                person.getFirstName().equals(firstName) && person.getLastName().equals(lastName));

        if (!wasRemoved) throw new PersonNotFoundException("The person: " + firstName + " " + lastName + "was not found!");
    }

    private void updatePersonInformation(Person current, Person update) {
        current.setAddress(update.getAddress());
        current.setCity(update.getCity());
        current.setZip(update.getZip());
        current.setEmail(update.getEmail());
        current.setPhone(update.getPhone());
    }

}
