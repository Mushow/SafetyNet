package uk.mushow.safetynet.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // ADD TEST ORDER BECAUSE UPDATE DOESNT PASS (DELETE IS EXECUTED BEFORE)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void addMedicalRecord_Valid_ShouldReturnCreated() throws Exception {
        String validMedicalRecordJson = """
                {
                    "firstName" : "Reginold",
                    "lastName" : "Walker",
                    "birthdate" : "08/30/1979",
                    "medications" : [ "paracetamol:500mg" ],
                    "allergies" : [ "illisoxian" ]
                }
                """;

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMedicalRecordJson))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void updateMedicalRecord_Valid_ShouldReturnOk() throws Exception {
        String validMedicalRecordJson = """
                {
                    "firstName" : "Reginold",
                    "lastName" : "Walker",
                    "birthdate" : "08/30/1979",
                    "medications" : [ "paracetamol:500mg" ],
                    "allergies" : [ "illisoxian" ]
                }
                """;

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMedicalRecordJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medications[0]").value("paracetamol:500mg"));
    }

    @Test
    @Order(3)
    public void deleteMedicalRecord_Valid_ShouldReturnNoContent() throws Exception {
        String validMedicalRecordJsonToDelete = """
                {
                    "firstName" : "Reginold",
                    "lastName" : "Walker",
                    "birthdate" : "08/30/1979",
                    "medications" : [ "thradox:700mg" ],
                    "allergies" : [ "illisoxian" ]
                }
                """;

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMedicalRecordJsonToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    public void handleValidationExceptions_ShouldReturnBadRequest() throws Exception {
        String invalidMedicalRecordJson = """
                {
                    "firstName": "",
                    "lastName": "",
                    "birthdate": "invalid-date",
                    "medications": [],
                    "allergies": []
                }
                """;

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMedicalRecordJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").value("First name is mandatory"))
                .andExpect(jsonPath("$.lastName").value("Last name is mandatory"));
    }
}
