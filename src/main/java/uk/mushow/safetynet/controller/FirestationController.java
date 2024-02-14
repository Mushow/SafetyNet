package uk.mushow.safetynet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.dto.FirestationDTO;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.service.FirestationService;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public ResponseEntity<FirestationDTO> getCoverage(@RequestParam("stationNumber") int stationNumber) {
        try {
            FirestationDTO firestationDTO = firestationService.getFirestationCoverage(stationNumber);
            return ResponseEntity.ok(firestationDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
        try {
            firestationService.updateFirestation(firestation);
            return ResponseEntity.ok(firestation);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestBody Firestation firestation) {
        try {
            firestationService.deleteFirestation(firestation);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}