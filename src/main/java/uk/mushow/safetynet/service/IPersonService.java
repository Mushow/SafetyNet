package uk.mushow.safetynet.service;

import uk.mushow.safetynet.model.Person;

public interface IPersonService {

    void addPerson(Person person);

    void updatePerson(Person person);

    void deletePerson(Person person);

}
