package uk.mushow.safetynet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.service.FirestationService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerTest {

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
        MvcResult result = mockMvc.perform(get("/firestation")
                        .param("stationNumber", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int statusCode = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        // Print response status code
        System.out.println("Response Status Code: " + statusCode);

        // Print JSON response
        System.out.println("JSON Response: " + jsonResponse);

        // Add your assertions here
    }


    @Test
    public void getFirestationCoverage_StationNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("Station not found"))
                .when(firestationService).getFirestationCoverage(eq(10));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
