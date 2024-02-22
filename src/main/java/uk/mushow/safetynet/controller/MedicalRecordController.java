package uk.mushow.safetynet.controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.service.MedicalRecordService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        log.debug("Attempting to add medical record: {}", medicalRecord);
        medicalRecordService.addMedicalRecord(medicalRecord);
        log.info("Medical record added successfully: {}", medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecord);
    }

    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        log.debug("Attempting to update medical record: {}", medicalRecord);
         medicalRecordService.updateMedicalRecord(medicalRecord);
        log.info("Medical record updated successfully: {}", medicalRecord);
        return ResponseEntity.ok(medicalRecord);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        log.debug("Attempting to delete medical record: {}", medicalRecord);
        medicalRecordService.deleteMedicalRecord(medicalRecord);
        log.info("Medical record deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        log.debug("Validation errors occurred: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.error("Error: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }
}
