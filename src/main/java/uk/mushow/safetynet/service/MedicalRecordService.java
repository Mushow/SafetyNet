package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.MedicalRecordNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.repository.MedicalRecordRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

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

    public MedicalRecord getByName(String firstName, String lastName) {
        return medicalRecordRepository.read(firstName, lastName);
    }

    public int getAge(MedicalRecord medicalRecord) {
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), getDateFormat());
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public DateTimeFormatter getDateFormat() {
        return DateTimeFormatter.ofPattern("MM/dd/yyyy");
    }

}
