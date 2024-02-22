package uk.mushow.safetynet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.service.FirestationService;
import uk.mushow.safetynet.service.PersonService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GenericControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private FirestationService firestationService;

    @Test
    public void getChildAlertShouldReturnOk() throws Exception {
        String address = "1509 Culver St";
        mockMvc.perform(get("/childAlert?address=" + address))
                .andExpect(status().isOk());
    }

    @Test
    public void getChildAlertShouldReturnNotFound() throws Exception {
        String address = "1509 Culver St";
        doThrow(new NotFoundException("Address not found: " + address))
                .when(personService).getChildAlertByAddress(any(String.class));

        mockMvc.perform(get("/childAlert?address=" + address))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPhoneAlertShouldReturnOk() throws Exception {
        int stationNumber = 1;
        mockMvc.perform(get("/phoneAlert?firestation=" + stationNumber))
                .andExpect(status().isOk());
    }

    @Test
    public void getPhoneAlertShouldReturnNotFound() throws Exception {
        int stationNumber = 1;
        doThrow(new NotFoundException("Station number not found: " + stationNumber))
                .when(firestationService).getPhoneAlertByStationNumber(eq(stationNumber));

        mockMvc.perform(get("/phoneAlert?firestation=" + stationNumber))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fireShouldReturnOk() throws Exception {
        String address = "1509 Culver St";
        mockMvc.perform(get("/fire?address=" + address))
                .andExpect(status().isOk());
    }

    @Test
    public void fireShouldReturnNotFound() throws Exception {
        String address = "Unknown Address";
        doThrow(new NotFoundException("Address not found: " + address))
                .when(personService).getFireAlertByAddress(anyString());

        mockMvc.perform(get("/fire?address=" + address))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFloodAlertByStationsShouldReturnOk() throws Exception {
        mockMvc.perform(get("/flood/stations?stations=1,2"))
                .andExpect(status().isOk());
    }

    @Test
    public void getFloodAlertByStationsShouldReturnNotFound() throws Exception {
        List<Integer> stations = Arrays.asList(100, 101);
        doThrow(new NotFoundException("Stations not found: " + stations))
                .when(personService).getFloodAlertByStations(anyList());

        mockMvc.perform(get("/flood/stations?stations=100,101"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPersonInfoShouldReturnOk() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        mockMvc.perform(get("/personInfo?firstName=" + firstName + "&lastName=" + lastName))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersonInfoShouldReturnNotFound() throws Exception {
        String firstName = "Unknown";
        String lastName = "Person";
        doThrow(new NotFoundException("Person not found: " + firstName + " " + lastName))
                .when(personService).getPersonInfo(anyString(), anyString());

        mockMvc.perform(get("/personInfo?firstName=" + firstName + "&lastName=" + lastName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCommunityEmailShouldReturnOk() throws Exception {
        String city = "Culver";
        mockMvc.perform(get("/communityEmail?city=" + city))
                .andExpect(status().isOk());
    }

    @Test
    public void getCommunityEmailShouldReturnNotFound() throws Exception {
        String city = "UnknownCity";
        doThrow(new NotFoundException("City not found: " + city))
                .when(personService).getCommunityEmail(anyString());

        mockMvc.perform(get("/communityEmail?city=" + city))
                .andExpect(status().isNotFound());
    }

}
