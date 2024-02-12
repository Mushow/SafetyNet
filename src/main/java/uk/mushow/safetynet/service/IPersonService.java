package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.model.Person;

public interface IPersonService {

    void addPerson(Person person);

    void updatePerson(Person person) throws PersonNotFoundException;

    void deletePerson(Person person) throws PersonNotFoundException;

}
