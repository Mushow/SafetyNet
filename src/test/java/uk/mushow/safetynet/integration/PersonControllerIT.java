package uk.mushow.safetynet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.service.PersonService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void addPersonShouldReturnCreated() throws Exception {
        String personJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "address": "1234 Main St",
                    "city": "Anytown",
                    "zip": "123456",
                    "phone": "123-456-7890",
                    "email": "john.doe@example.com"
                }""";

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void updatePersonShouldReturnOk() throws Exception {
        String personJsonToUpdate = """
        {
            "firstName" : "Jonanathan",
            "lastName" : "Marrack",
            "address" : "29 15th St",
            "city" : "Los Angeles",
            "zip" : "19923",
            "phone" : "841-874-6513",
            "email" : "drk@email.com"
          }""";

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJsonToUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jonanathan"))
                .andExpect(jsonPath("$.lastName").value("Marrack"))
                .andExpect(jsonPath("$.city").value("Los Angeles"));
    }

    @Test
    public void deletePersonShouldReturnNoContent() throws Exception {
        String personJsonToDelete = """
         {
            "firstName" : "Jonanathan",
            "lastName" : "Marrack",
            "address" : "29 15th St",
            "city" : "Los Angeles",
            "zip" : "19923",
            "phone" : "841-874-6513",
            "email" : "drk@email.com"
          }""";

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJsonToDelete))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updatePerson_WhenPersonNotFound_ShouldReturnNotFoundStatus() throws Exception {
        doThrow(new NotFoundException("Person not found"))
                .when(personService).updatePerson(any(Person.class));


        String personJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\", \"address\":\"123 Main St\", \"city\":\"Springfield\", \"zip\":\"123456\", \"phone\":\"123-456-7890\", \"email\":\"john.doe@example.com\"}";

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletePerson_WhenPersonNotFound_ShouldReturnNotFoundStatus() throws Exception {
        doThrow(new NotFoundException("Person not found"))
                .when(personService).deletePerson(any(Person.class));

        String personJson = "{\"firstName\":\"John\", \"lastName\":\"Doe\", \"address\":\"123 Main St\", \"city\":\"Springfield\", \"zip\":\"123456\", \"phone\":\"123-456-7890\", \"email\":\"john.doe@example.com\"}";
        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenPostRequestToPersonAndDataIsInvalid_thenRespondWithValidationErrors() throws Exception {
        String personJson = "{\"name\":\"\", \"firstName\":\"\"}";

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").value("First name is mandatory"))
                .andExpect(jsonPath("$.lastName").value("Last name is mandatory"));
    }

}

