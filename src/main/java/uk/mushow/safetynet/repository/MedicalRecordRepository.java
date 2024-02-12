package uk.mushow.safetynet.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.exception.MedicalRecordNotFoundException;
import uk.mushow.safetynet.model.MedicalRecord;

import java.util.Optional;

@Repository
public class MedicalRecordRepository {

    @Autowired
    private DataWrapper dataWrapper;

    public MedicalRecordRepository(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public void create(MedicalRecord medicalRecord) {
        dataWrapper.getMedicalRecords().add(medicalRecord);
    }

    public void update(MedicalRecord updatedMedicalRecord) throws MedicalRecordNotFoundException {
        String id = updatedMedicalRecord.getFirstName() + updatedMedicalRecord.getLastName();
        boolean found = false;

        for (MedicalRecord currentMedicalRecord : dataWrapper.getMedicalRecords()) {
            String currentId = currentMedicalRecord.getFirstName() + currentMedicalRecord.getLastName();
            if (currentId.equals(id)) {
                updateMedicalRecordInformation(currentMedicalRecord, updatedMedicalRecord);
                break;
            }
        }
        if (!found) {
            throw new MedicalRecordNotFoundException("The medical record for: " + updatedMedicalRecord.getFirstName() + " " + updatedMedicalRecord.getLastName() + " was not found!");
        }
    }

    public void delete(MedicalRecord medicalRecordToDelete) throws MedicalRecordNotFoundException {
        String firstName = medicalRecordToDelete.getFirstName();
        String lastName = medicalRecordToDelete.getLastName();
        boolean wasRemoved = dataWrapper.getMedicalRecords().removeIf(medicalRecord ->
                medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName));

        if (!wasRemoved) {
            throw new MedicalRecordNotFoundException("The medical record for: " + firstName + " " + lastName + " was not found!");
        }
    }

    private void updateMedicalRecordInformation(MedicalRecord current, MedicalRecord update) {
        current.setBirthdate(update.getBirthdate());
        current.setMedications(update.getMedications());
        current.setAllergies(update.getAllergies());
    }

    public MedicalRecord read(String firstName, String lastName) {
        Optional<MedicalRecord> matchingRecord = dataWrapper.getMedicalRecords().stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                .findFirst();

        return matchingRecord.orElse(null);
    }

}
