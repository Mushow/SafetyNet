package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.AddressNotFoundException;
import uk.mushow.safetynet.exception.CityNotFoundException;
import uk.mushow.safetynet.dto.*;
import uk.mushow.safetynet.exception.PersonNotFoundException;
import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.FirestationRepository;
import uk.mushow.safetynet.repository.PersonRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordService medicalRecordService;

    public PersonService(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordService medicalRecordService) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
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

    public List<ChildDTO> getChildAlertByAddress(String address) throws AddressNotFoundException {
        List<Person> personsAtAddress = personRepository.findPersonByPredicate(person -> person.getAddress().equals(address));

        if (personsAtAddress.isEmpty()) throw new AddressNotFoundException("Address not found: " + address);

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



    public List<ResidentDTO> getFireAlertByAddress(String address) throws AddressNotFoundException {
        List<Person> persons = personRepository.findPersonByPredicate(person -> person.getAddress().equals(address));

        if (persons.isEmpty()) throw new AddressNotFoundException("Address not found: " + address);

        return getResidentDTO(persons);
    }


    public List<FloodDTO> getFloodAlertByStations(List<Integer> stations) throws StationNotFoundException {
        List<FloodDTO> floodAlerts = new ArrayList<>();

        for (Integer station : stations) {
            List<String> stationAddresses = firestationRepository.findAddressesByStationNumber(station);
            Map<String, List<ResidentDTO>> householdsByAddress = new HashMap<>();

            for (String address : stationAddresses) {
                List<Person> personsAtAddress = personRepository.findPersonByPredicate(person -> person.getAddress().equals(address));
                List<ResidentDTO> residents = getResidentDTO(personsAtAddress);

                householdsByAddress.put(address, residents);
            }

            floodAlerts.add(new FloodDTO(householdsByAddress));
        }

        return floodAlerts;
    }

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws PersonNotFoundException {
        List<Person> people = personRepository.findPersonByPredicate(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName));
        if (people.isEmpty()) {
            throw new PersonNotFoundException("Person not found: " + firstName + " " + lastName);
        }
        return people.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecordService.getByName(person.getFirstName(), person.getLastName());
                    int age = getAge(medicalRecord);
                    MedicalInfoDTO medicalInfoDTO = new MedicalInfoDTO(medicalRecord.getMedications(), medicalRecord.getAllergies());

                    return new PersonInfoDTO(person.getLastName(), person.getAddress(), age, person.getEmail(), medicalInfoDTO);
                })
                .collect(Collectors.toList());
    }

    public List<String> getCommunityEmail(String city) throws CityNotFoundException {
        List<Person> people = personRepository.findPersonByPredicate(person -> person.getCity().equals(city));
        if (people.isEmpty()) {
            throw new CityNotFoundException("City not found: " + city);
        }
        return people.stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    private List<ResidentDTO> getResidentDTO(List<Person> persons) {
        return persons.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecordService.getByName(person.getFirstName(), person.getLastName());
                    int age = getAge(medicalRecord);
                    MedicalInfoDTO medicalInfoDTO = new MedicalInfoDTO(medicalRecord.getMedications(), medicalRecord.getAllergies());

                    return new ResidentDTO(person.getLastName(), person.getPhone(), age, medicalInfoDTO);
                })
                .collect(Collectors.toList());
    }

}
