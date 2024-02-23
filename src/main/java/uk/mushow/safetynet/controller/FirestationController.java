package uk.mushow.safetynet.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.dto.FirestationDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.service.FirestationService;

@RestController
@RequestMapping("/firestation")
@Log4j2
public class FirestationController {

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public ResponseEntity<FirestationDTO> getCoverage(@RequestParam("stationNumber") int stationNumber) throws NotFoundException {
        FirestationDTO firestationDTO = firestationService.getFirestationCoverage(stationNumber);
        return ResponseEntity.ok(firestationDTO);
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body(firestation);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) throws NotFoundException {
        firestationService.updateFirestation(firestation);
        return ResponseEntity.ok(firestation);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFirestation(@RequestBody Firestation firestation) throws NotFoundException {
        firestationService.deleteFirestation(firestation);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.error("Error: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}