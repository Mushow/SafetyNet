package uk.mushow.safetynet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    // Assuming there's an endpoint to get firestation by station number
    @Test
    public void getFirestationCoverage_ValidStation_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").isNotEmpty())
                .andExpect(jsonPath("$.numberOfChildren").isNotEmpty())
                .andExpect(jsonPath("$.personsCovered").isArray());
    }
}
