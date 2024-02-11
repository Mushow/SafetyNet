package uk.mushow.safetynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.PersonRepository;

@Service
public class PersonService implements IPersonService {


    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person addPerson(Person person) {
        return personRepository.create(person);
    }

    @Override
    public Person updatePerson(Person person) {
        return personRepository.update(person);
    }

    @Override
    public void deletePerson(Person person) {
        personRepository.delete(person.getFirstName(), person.getLastName());
    }

}
