package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Person;

public interface IPersonService {

    void addPerson(Person person);

    void updatePerson(Person person) throws NotFoundException;

    void deletePerson(Person person) throws NotFoundException;

}
