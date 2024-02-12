package uk.mushow.safetynet.repository;

import org.springframework.stereotype.Repository;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.model.Person;

@Repository
public class PersonRepository {

    private final DataWrapper dataWrapper;

    public PersonRepository(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public void create(Person person) {
        dataWrapper.persons().add(person);
    }

    public void update(Person updatedPerson) throws PersonNotFoundException {
        String id = updatedPerson.getFirstName() + updatedPerson.getLastName();
        boolean found = false;

        for (Person currentPerson : dataWrapper.persons()) {
            String currentId = currentPerson.getFirstName() + currentPerson.getLastName();
            if (currentId.equals(id)) {
                updatePersonInformation(currentPerson, updatedPerson);
                found = true;
                break;
            }
        }

        if(!found) throw new PersonNotFoundException("The person: " + updatedPerson.getFirstName() + " " + updatedPerson.getLastName() + "was not found!");
    }

    public void delete(Person personToDelete) throws PersonNotFoundException {
        String firstName = personToDelete.getFirstName();
        String lastName = personToDelete.getLastName();
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
