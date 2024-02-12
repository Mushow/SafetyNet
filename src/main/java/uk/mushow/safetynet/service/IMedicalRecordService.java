package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.MedicalRecordNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;

public interface IMedicalRecordService {

    void addMedicalRecord(MedicalRecord medicalRecord);

    void updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException;

    void deleteMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException;

}
