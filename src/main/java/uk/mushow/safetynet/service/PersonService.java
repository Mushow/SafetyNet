package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.PersonRepository;

@Service
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void addPerson(Person person) {
        personRepository.create(person);
    }

    @Override
    public void updatePerson(Person person) throws PersonNotFoundException {
        personRepository.update(person);
    }

    @Override
    public void deletePerson(Person person) throws PersonNotFoundException {
        personRepository.delete(person);
    }

}
