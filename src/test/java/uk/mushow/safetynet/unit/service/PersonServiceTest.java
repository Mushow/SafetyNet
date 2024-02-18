package uk.mushow.safetynet.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.mushow.safetynet.dto.ChildDTO;
import uk.mushow.safetynet.dto.FloodDTO;
import uk.mushow.safetynet.dto.PersonInfoDTO;
import uk.mushow.safetynet.dto.ResidentDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.FirestationRepository;
import uk.mushow.safetynet.repository.PersonRepository;
import uk.mushow.safetynet.service.MedicalRecordService;
import uk.mushow.safetynet.service.PersonService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getChildAlertByAddress_ReturnsListOfChildrenAndTheirFamilies() throws Exception {
        List<Person> persons = List.of(
                new Person("Child", "One", "Address", "City", "Zip", "Phone", "Email"),
                new Person("Family", "Member", "Address", "City", "Zip", "Phone", "Email")
        );
        MedicalRecord childRecord = new MedicalRecord("Child", "One", "01/01/2010", List.of(), List.of());
        MedicalRecord familyRecord = new MedicalRecord("Family", "Member", "01/01/1980", List.of(), List.of());

        when(personRepository.findPersonByPredicate(any())).thenReturn(persons);
        when(medicalRecordService.getByName("Child", "One")).thenReturn(childRecord);
        when(medicalRecordService.getByName("Family", "Member")).thenReturn(familyRecord);

        when(medicalRecordService.getAge(eq(childRecord))).thenReturn(12);
        when(medicalRecordService.getAge(eq(familyRecord))).thenReturn(40);

        List<ChildDTO> result = personService.getChildAlertByAddress("Address");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        ChildDTO childDTO = result.get(0);
        assertEquals("Child", childDTO.firstName());
        assertEquals(12, childDTO.age());
        assertFalse(childDTO.householdMembers().isEmpty());
    }


    @Test
    void whenAddressHasNoResidents_thenThrowsNotFoundException() {
        String testAddress = "Empty Address";
        when(personRepository.findPersonByPredicate(any())).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> personService.getChildAlertByAddress(testAddress));
        verify(personRepository).findPersonByPredicate(any());
    }

    @Test
    void addPerson_CallsRepositoryCreate() {
        Person person = new Person("John", "Doe", "1234 Main St", "Anytown", "123456", "123-456-7890", "john.doe@example.com");
        personService.addPerson(person);
        verify(personRepository).create(person);
    }

    @Test
    void updatePerson_CallsRepositoryUpdate() throws NotFoundException {
        Person person = new Person("John", "Doe", "1234 Main St", "Anytown", "123456", "123-456-7890", "john.doe@example.com");
        personService.updatePerson(person);
        verify(personRepository).update(person);
    }

    @Test
    void deletePerson_CallsRepositoryDelete() throws NotFoundException {
        Person person = new Person("John", "Doe", "1234 Main St", "Anytown", "123456", "123-456-7890", "john.doe@example.com");
        personService.deletePerson(person);
        verify(personRepository).delete(person);
    }

    @Test
    public void getFireAlertByAddress_ReturnsResidentsInfo() throws NotFoundException {
        String testAddress = "123 Test St";
        Person testPerson = new Person("John", "Doe", testAddress, "Test City", "00000", "123-456-7890", "john.doe@example.com");
        MedicalRecord testMedicalRecord = new MedicalRecord("John", "Doe", "01/01/1980", List.of("medication1"), List.of("allergy1"));

        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of(testPerson));
        when(medicalRecordService.getByName("John", "Doe")).thenReturn(testMedicalRecord);
        when(medicalRecordService.getAge(testMedicalRecord)).thenReturn(40);

        List<ResidentDTO> result = personService.getFireAlertByAddress(testAddress);

        assertFalse(result.isEmpty(), "Expected non-empty result");
        assertEquals(1, result.size(), "Expected one resident in the result");
        ResidentDTO residentDTO = result.get(0);
        assertEquals("Doe", residentDTO.name(), "Expected last name to match");
        assertEquals("123-456-7890", residentDTO.phone(), "Expected phone to match");
        assertEquals(40, residentDTO.age(), "Expected age to match");
        assertEquals(List.of("medication1"), residentDTO.medicalInfo().medications(), "Expected medications to match");
        assertEquals(List.of("allergy1"), residentDTO.medicalInfo().allergies(), "Expected allergies to match");
    }

    @Test
    public void getPersonInfo_ReturnsPersonInfoDTOs() throws NotFoundException {
        String firstName = "John";
        String lastName = "Doe";
        Person testPerson = new Person(firstName, lastName, "123 Test St", "Test City", "00000", "123-456-7890", "john.doe@example.com");
        MedicalRecord testMedicalRecord = new MedicalRecord(firstName, lastName, "01/01/1980", List.of("medication1"), List.of("allergy1"));

        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of(testPerson));
        when(medicalRecordService.getByName(firstName, lastName)).thenReturn(testMedicalRecord);
        when(medicalRecordService.getAge(testMedicalRecord)).thenReturn(40);

        List<PersonInfoDTO> result = personService.getPersonInfo(firstName, lastName);

        assertFalse(result.isEmpty(), "Expected non-empty result");
        assertEquals(1, result.size(), "Expected result size to be 1");
        PersonInfoDTO dto = result.get(0);
        assertEquals(lastName, dto.lastName(), "Expected lastName to match");
        assertEquals("123 Test St", dto.address(), "Expected address to match");
        assertEquals(40, dto.age(), "Expected age to match");
        assertEquals("john.doe@example.com", dto.email(), "Expected email to match");
        assertNotNull(dto.medicalInfo(), "Expected medicalInfo to be not null");
        assertEquals(List.of("medication1"), dto.medicalInfo().medications(), "Expected medications to match");
        assertEquals(List.of("allergy1"), dto.medicalInfo().allergies(), "Expected allergies to match");
    }

    @Test
    public void getCommunityEmail_ReturnsEmailList() throws NotFoundException {
        String city = "Test City";
        Person testPerson = new Person("John", "Doe", "123 Test St", city, "00000", "123-456-7890", "john.doe@example.com");

        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of(testPerson));

        List<String> result = personService.getCommunityEmail(city);

        assertFalse(result.isEmpty(), "Expected non-empty result");
        assertEquals(1, result.size(), "Expected result size to be 1");
        assertTrue(result.contains("john.doe@example.com"), "Expected email list to contain the test person's email");
    }

    @Test
    void getPersonInfo_ThrowsNotFoundException() {
        String firstName = "Nonexistent";
        String lastName = "Person";

        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> personService.getPersonInfo(firstName, lastName), "Expected NotFoundException to be thrown when person is not found");
    }

    @Test
    void getCommunityEmail_ThrowsNotFoundException() {
        String city = "Nonexistent City";

        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> personService.getCommunityEmail(city), "Expected NotFoundException to be thrown when city has no community emails");
    }

    @Test
    public void getFloodAlertByStations_ReturnsFloodInfoForStations() throws NotFoundException {
        int stationNumber = 1;
        String testAddress = "123 Test St";
        Person testPerson = new Person("John", "Doe", testAddress, "Test City", "00000", "123-456-7890", "john.doe@example.com");
        MedicalRecord testMedicalRecord = new MedicalRecord("John", "Doe", "01/01/1980", List.of("medication1"), List.of("allergy1"));

        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(List.of(testAddress));
        when(personRepository.findPersonByPredicate(any())).thenReturn(List.of(testPerson));
        when(medicalRecordService.getByName("John", "Doe")).thenReturn(testMedicalRecord);
        when(medicalRecordService.getAge(testMedicalRecord)).thenReturn(40);

        List<FloodDTO> result = personService.getFloodAlertByStations(List.of(stationNumber));

        assertFalse(result.isEmpty(), "Expected non-empty result for flood stations");
        assertEquals(1, result.size(), "Expected result for one station");
        FloodDTO floodDTO = result.get(0);
        assertTrue(floodDTO.householdsByAddress().containsKey(testAddress), "Expected address in the result");
        List<ResidentDTO> residents = floodDTO.householdsByAddress().get(testAddress);
        assertNotNull(residents, "Expected non-null residents list");
        assertFalse(residents.isEmpty(), "Expected non-empty residents list");
        ResidentDTO residentDTO = residents.get(0);
        assertEquals("Doe", residentDTO.name(), "Expected last name to match");
        assertEquals("123-456-7890", residentDTO.phone(), "Expected phone to match");
        assertEquals(40, residentDTO.age(), "Expected age to match");
        assertEquals(List.of("medication1"), residentDTO.medicalInfo().medications(), "Expected medications to match");
        assertEquals(List.of("allergy1"), residentDTO.medicalInfo().allergies(), "Expected allergies to match");
    }

}
