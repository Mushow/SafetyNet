package uk.mushow.safetynet.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.mushow.safetynet.dto.FirestationDTO;
import uk.mushow.safetynet.dto.PersonCoveredDTO;
import uk.mushow.safetynet.dto.PhoneAlertDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.repository.FirestationRepository;
import uk.mushow.safetynet.service.FirestationService;
import uk.mushow.safetynet.service.PersonService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private FirestationService firestationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFirestation_CallsRepositoryCreate() {
        Firestation firestation = new Firestation("address", 1);
        firestationService.addFirestation(firestation);
        verify(firestationRepository, times(1)).create(firestation);
    }

    @Test
    void updateFirestation_CallsRepositoryUpdate() throws NotFoundException {
        Firestation firestation = new Firestation("New Address", 1);

        doNothing().when(firestationRepository).update(firestation);

        firestationService.updateFirestation(firestation);

        verify(firestationRepository, times(1)).update(firestation);
    }

    @Test
    void deleteFirestation_CallsRepositoryDelete() throws NotFoundException {
        Firestation firestation = new Firestation("address", 1);

        doNothing().when(firestationRepository).delete(firestation);

        firestationService.deleteFirestation(firestation);

        verify(firestationRepository, times(1)).delete(firestation);
    }

    @Test
    void getFirestationCoverage_ReturnsCorrectCoverage() throws NotFoundException {
        int stationNumber = 1;
        List<String> addresses = List.of("Address 1", "Address 2");
        List<PersonCoveredDTO> personsAddress1 = List.of(
                new PersonCoveredDTO("John", "Doe", "Address 1", "123-456-7890"),
                new PersonCoveredDTO("Jane", "Doe", "Address 1", "123-456-7891")
        );
        List<PersonCoveredDTO> personsAddress2 = List.of(
                new PersonCoveredDTO("Jim", "Beam", "Address 2", "987-654-3210")
        );

        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        when(firestationRepository.findPersonsByAddress("Address 1")).thenReturn(personsAddress1);
        when(firestationRepository.findPersonsByAddress("Address 2")).thenReturn(personsAddress2);
        when(personService.isAdult(anyString(), anyString())).thenReturn(true);

        FirestationDTO result = firestationService.getFirestationCoverage(stationNumber);

        assertEquals(3, result.personsCovered().size());
        assertEquals(3, result.numberOfAdults());
        assertEquals(0, result.numberOfChildren());
    }

    @Test
    void getFirestationCoverage_ThrowsNotFoundException_WhenStationNotFound() {
        int stationNumber = 999;
        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> firestationService.getFirestationCoverage(stationNumber));
    }

    @Test
    void getPhoneAlertByStationNumber_ReturnsPhoneNumbers() throws NotFoundException {
        int stationNumber = 1;
        List<String> addresses = List.of("Address 1");
        List<String> phoneNumbers = List.of("123-456-7890", "987-654-3210");

        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        when(firestationRepository.findPhoneNumbersByAddress("Address 1")).thenReturn(phoneNumbers);

        PhoneAlertDTO result = firestationService.getPhoneAlertByStationNumber(stationNumber);

        assertEquals(2, result.phoneNumbers().size());
        assertTrue(result.phoneNumbers().containsAll(phoneNumbers));
    }

    @Test
    void getPhoneAlertByStationNumber_ThrowsNotFoundException_WhenStationNotFound() {
        int stationNumber = 999;
        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> firestationService.getPhoneAlertByStationNumber(stationNumber));
    }


}
