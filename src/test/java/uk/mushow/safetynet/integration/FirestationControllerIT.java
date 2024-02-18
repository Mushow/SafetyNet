package uk.mushow.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.mushow.safetynet.dto.FirestationDTO;
import uk.mushow.safetynet.dto.PersonCoveredDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.service.FirestationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private FirestationService firestationService;

    @Test
    public void addFirestation_Valid_ShouldReturnCreated() throws Exception {
        String validFirestationJson = """
                {
                    "address": "1509 Culver St",
                    "station": 3
                }
                """;

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFirestationJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateFirestation_Valid_ShouldReturnOk() throws Exception {
        String validFirestationJson = """
                {
                    "address": "1509 Culver St",
                    "station": 2
                }
                """;

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFirestationJson))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFirestation_NotFound_ShouldReturnNotFound() throws Exception {
        String validFirestationJson = """
                {
                    "address": "NonExistingAddress",
                    "station": 2
                }
                """;

        doThrow(new NotFoundException("Firestation not found")).when(firestationService).updateFirestation(any());

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFirestationJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFirestation_NotFound_ShouldReturnNotFound() throws Exception {
        String validFirestationJson = """
                {
                    "address": "NonExistingAddress",
                    "station": 2
                }
                """;

        doThrow(new NotFoundException("Firestation not found")).when(firestationService).deleteFirestation(any());

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFirestationJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteFirestation_Valid_ShouldReturnNoContent() throws Exception {
        String validFirestationJsonToDelete = """
                {
                    "address": "1509 Culver St",
                    "station": 2
                }
                """;

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFirestationJsonToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getFirestationCoverage_ValidStation_ShouldReturnOk() throws Exception {
        int stationNumber = 2;
        FirestationDTO mockResponse = new FirestationDTO(
                List.of(new PersonCoveredDTO(
                                "John",
                                "Doe",
                                "1234 Main St",
                                "123-456-7890")), 1, 0);

        when(firestationService.getFirestationCoverage(eq(stationNumber))).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(get("/firestation")
                        .param("stationNumber", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        assertEquals(200, result.getResponse().getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = result.getResponse().getContentAsString();
        FirestationDTO actualFirestationDTO = objectMapper.readValue(jsonResponse, FirestationDTO.class);
        assertEquals(mockResponse, actualFirestationDTO, "The returned FirestationDTO does not match the expected");
    }

}
