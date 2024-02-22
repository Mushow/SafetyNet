package uk.mushow.safetynet.data;

import lombok.Data;
import org.springframework.stereotype.Component;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class DataWrapper {

    private List<Person> persons = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

}
