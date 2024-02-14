package uk.mushow.safetynet.controller;

import jakarta.validation.Valid;
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
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        try {
            medicalRecordService.updateMedicalRecord(medicalRecord);
            return ResponseEntity.ok(medicalRecord);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        try {
            medicalRecordService.deleteMedicalRecord(medicalRecord);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Field name and the error message for each validation error
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
