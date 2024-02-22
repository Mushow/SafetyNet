package uk.mushow.safetynet.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.service.PersonService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        log.info("Trying to add person: {}", person);
        personService.addPerson(person);
        log.info("Person added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(person);
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@Valid @RequestBody Person person) throws NotFoundException {
        log.info("Trying to update person: {}", person);
        personService.updatePerson(person);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@Valid @RequestBody Person person) throws NotFoundException {
        log.info("Trying to delete person: {}", person);
        personService.deletePerson(person);
        log.info("Person deleted successfully");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.error("Error: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
