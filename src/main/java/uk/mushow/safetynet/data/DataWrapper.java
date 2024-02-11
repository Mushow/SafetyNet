package uk.mushow.safetynet.data;

import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.model.MedicalRecord;
import uk.mushow.safetynet.model.Person;

import java.util.List;

public record DataWrapper(List<Person> persons, List<Firestation> firestations, List<MedicalRecord> medicalRecords) {}