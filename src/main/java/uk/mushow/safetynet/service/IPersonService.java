package uk.mushow.safetynet.service;

import uk.mushow.safetynet.model.Person;

public interface IPersonService {

    Person addPerson(Person person);

    Person updatePerson(Person person);

    void deletePerson(Person person);

}
