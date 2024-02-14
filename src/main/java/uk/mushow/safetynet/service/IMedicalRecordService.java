package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;

public interface IMedicalRecordService {

    void addMedicalRecord(MedicalRecord medicalRecord);

    void updateMedicalRecord(MedicalRecord medicalRecord) throws NotFoundException;

    void deleteMedicalRecord(MedicalRecord medicalRecord) throws NotFoundException;

}
