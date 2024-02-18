package uk.mushow.safetynet.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

    @Mock
    private DataWrapper dataWrapper;

    private PersonRepository personRepository;

    private final Person person = new Person("John", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "john.doe@example.com");


    @BeforeEach
    void setUp() {
        personRepository = new PersonRepository(dataWrapper);
    }

    @Test
    void create_AddsPersonToList() {
        List<Person> personList = new ArrayList<>();
        when(dataWrapper.getPersons()).thenReturn(personList);

        personRepository.create(person);

        assertTrue(personList.contains(person));
    }

    @Test
    void update_ExistingPerson_UpdatesInformation() throws NotFoundException {
        List<Person> persons = new ArrayList<>(List.of(person));
        when(dataWrapper.getPersons()).thenReturn(persons);

        // Create an updated person instance
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "Paris", "75001", "987-654-3210", "john.updated@example.com");
        personRepository.update(updatedPerson);

        assertEquals("456 Elm St", persons.get(0).getAddress());
        assertEquals("987-654-3210", persons.get(0).getPhone());
    }

    @Test
    void update_NonExistingPerson_ThrowsNotFoundException() {
        when(dataWrapper.getPersons()).thenReturn(new ArrayList<>());

        Person nonExistingPerson = new Person("Jane", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "jane.doe@example.com");

        assertThrows(NotFoundException.class, () -> personRepository.update(nonExistingPerson));
    }

    @Test
    void delete_ExistingPerson_RemovesFromList() throws NotFoundException {
        List<Person> persons = new ArrayList<>(List.of(person));
        when(dataWrapper.getPersons()).thenReturn(persons);

        personRepository.delete(person);

        assertTrue(persons.isEmpty());
    }

    @Test
    void delete_NonExistingPerson_ThrowsNotFoundException() {
        when(dataWrapper.getPersons()).thenReturn(new ArrayList<>());

        Person nonExistingPerson = new Person("Jane", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "jane.doe@example.com");

        assertThrows(NotFoundException.class, () -> personRepository.delete(nonExistingPerson));
    }

    @Test
    void findPersonByPredicate_ReturnsCorrectPersons() {
        List<Person> persons = new ArrayList<>(List.of(person, new Person("Jane", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "jane.doe@example.com")));
        when(dataWrapper.getPersons()).thenReturn(persons);

        List<Person> result = personRepository.findPersonByPredicate(p -> p.getCity().equals("Paris"));

        assertTrue(result.contains(person));
        assertEquals(2, result.size());
    }


}
