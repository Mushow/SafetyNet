package uk.mushow.safetynet.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.dto.PersonCoveredDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.FirestationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {

    @Mock
    private DataWrapper dataWrapper;

    private FirestationRepository firestationRepository;

    @BeforeEach
    void setUp() {
        firestationRepository = new FirestationRepository(dataWrapper);
    }

    @Test
    void create_AddsFirestation() {
        Firestation firestation = new Firestation("123 Main St", 1);
        List<Firestation> firestations = new ArrayList<>();
        when(dataWrapper.getFirestations()).thenReturn(firestations);

        firestationRepository.create(firestation);

        assertTrue(firestations.contains(firestation));
    }

    @Test
    void update_ExistingFirestation_UpdatesFirestation() throws NotFoundException {
        Firestation existingFirestation = new Firestation("123 Main St", 1);
        List<Firestation> firestations = new ArrayList<>(List.of(existingFirestation));
        when(dataWrapper.getFirestations()).thenReturn(firestations);

        Firestation updatedFirestation = new Firestation("123 Main St", 2);
        firestationRepository.update(updatedFirestation);

        assertEquals(2, existingFirestation.getStation());
    }

    @Test
    void update_NonExistingFirestation_ThrowsNotFoundException() {
        when(dataWrapper.getFirestations()).thenReturn(new ArrayList<>());

        Firestation updatedFirestation = new Firestation("123 Main St", 2);

        assertThrows(NotFoundException.class, () -> firestationRepository.update(updatedFirestation));
    }

    @Test
    void delete_ExistingFirestation_RemovesFirestation() throws NotFoundException {
        Firestation firestation = new Firestation("123 Main St", 1);
        List<Firestation> firestations = new ArrayList<>(List.of(firestation));
        when(dataWrapper.getFirestations()).thenReturn(firestations);

        firestationRepository.delete(firestation);

        assertFalse(firestations.contains(firestation));
    }

    @Test
    void delete_NonExistingFirestation_ThrowsNotFoundException() {
        when(dataWrapper.getFirestations()).thenReturn(new ArrayList<>());

        Firestation firestation = new Firestation("123 Main St", 1);

        assertThrows(NotFoundException.class, () -> firestationRepository.delete(firestation));
    }

    @Test
    void findAddressesByStationNumber_ReturnsAddresses() {
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 1);
        when(dataWrapper.getFirestations()).thenReturn(List.of(firestation1, firestation2));

        List<String> addresses = firestationRepository.findAddressesByStationNumber(1);

        assertEquals(2, addresses.size());
        assertTrue(addresses.contains("123 Main St") && addresses.contains("456 Elm St"));
    }

    @Test
    void findPersonsByAddress_ReturnsPersons() {
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "Zip", "123-456-7890", "Email");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "Zip", "987-654-3210", "Email");
        when(dataWrapper.getPersons()).thenReturn(List.of(person1, person2));

        List<PersonCoveredDTO> persons = firestationRepository.findPersonsByAddress("123 Main St");

        assertEquals(2, persons.size());
        assertTrue(persons.stream().anyMatch(p -> p.phone().equals("123-456-7890")));
    }

    @Test
    void findPhoneNumbersByAddress_ReturnsPhoneNumbers() {
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "Zip", "123-456-7890", "Email");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "Zip", "987-654-3210", "Email");
        when(dataWrapper.getPersons()).thenReturn(List.of(person1, person2));

        List<String> phoneNumbers = firestationRepository.findPhoneNumbersByAddress("123 Main St");

        assertEquals(2, phoneNumbers.size());
        assertTrue(phoneNumbers.contains("123-456-7890") && phoneNumbers.contains("987-654-3210"));
    }

}

