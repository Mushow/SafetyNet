package uk.mushow.safetynet.data;

import lombok.Data;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;

import java.util.List;

@Data
public class DataWrapper {

    private final List<Person> persons;
    private final List<Firestation> firestations;
    private final List<MedicalRecord> medicalRecords;

}
