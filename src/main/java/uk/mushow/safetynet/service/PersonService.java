package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.dto.ChildDTO;
import uk.mushow.safetynet.dto.ChildFamilyDTO;
import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ChildDTO> getChildAlertByAddress(String address) {
        List<Person> personsAtAddress = personRepository.findPersonByPredicate(person -> person.getAddress().equals(address));
        List<ChildDTO> childAlerts = new ArrayList<>();

        List<Person> children = personsAtAddress.stream()
                .filter(person -> !isAdult(person.getFirstName(), person.getLastName()))
                .toList();

        for (Person child : children) {
            List<ChildFamilyDTO> householdMembers = personsAtAddress.stream()
                    .filter(person -> !person.equals(child))
                    .map(person -> new ChildFamilyDTO(person.getFirstName(), person.getLastName()))
                    .collect(Collectors.toList());

            int age = getAge(medicalRecordService.getByName(child.getFirstName(), child.getLastName()));

            childAlerts.add(new ChildDTO(child.getFirstName(), child.getLastName(), age, householdMembers));
        }

        return childAlerts;
    }

}
