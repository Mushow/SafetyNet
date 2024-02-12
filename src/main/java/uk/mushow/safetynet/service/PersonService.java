package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.PersonRepository;

@Service
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;

    public PersonService(PersonRepository personRepository, MedicalRecordService medicalRecordService) {
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
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

    private Integer getAge(MedicalRecord medicalRecord) {
        return medicalRecordService.getAge(medicalRecord);
    }

    public boolean isAdult(String firstName, String lastName) {
        MedicalRecord medicalRecord = medicalRecordService.getByName(firstName, lastName);
        return getAge(medicalRecord) >= 18;
    }

}
