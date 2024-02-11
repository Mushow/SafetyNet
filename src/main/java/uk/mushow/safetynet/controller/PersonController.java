package uk.mushow.safetynet.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.exceptions.PersonNotFoundException;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.service.PersonService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        Person savedPerson = personService.addPerson(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@Valid @RequestBody Person person) {
        try {
            Person updatedPerson = personService.updatePerson(person);
            return ResponseEntity.ok(updatedPerson);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@Valid @RequestBody Person person) {
        try {
            personService.deletePerson(person);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e ) {
            return ResponseEntity.notFound().build();
        }
    }

    //Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        //Field name and the error message for each validation error
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

}