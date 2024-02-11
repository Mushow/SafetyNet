package uk.mushow.safetynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.model.Person;
import uk.mushow.safetynet.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person savedPerson = personService.addPerson(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        Person updatedPerson = personService.updatePerson(person);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestBody Person person) {
        personService.deletePerson(person);
        return ResponseEntity.noContent().build();
    }
}
