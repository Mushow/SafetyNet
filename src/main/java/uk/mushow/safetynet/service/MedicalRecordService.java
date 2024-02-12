package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.MedicalRecordNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService implements IMedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.create(medicalRecord);
    }

    @Override
    public void updateMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        medicalRecordRepository.update(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(MedicalRecord medicalRecord) throws MedicalRecordNotFoundException {
        medicalRecordRepository.delete(medicalRecord);
    }
}
