package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.dto.FirestationDTO;
import uk.mushow.safetynet.dto.PersonCoveredDTO;
import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.repository.FirestationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FirestationService implements IFirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonService personService;

    public FirestationService(FirestationRepository firestationRepository, PersonService personService) {
        this.firestationRepository = firestationRepository;
        this.personService = personService;
    }

    @Override
    public void addFirestation(Firestation firestation) {
        firestationRepository.create(firestation);
    }

    @Override
    public void updateFirestation(Firestation firestation) throws StationNotFoundException {
        firestationRepository.update(firestation);
    }

    @Override
    public void deleteFirestation(Firestation firestation) throws StationNotFoundException {
        firestationRepository.delete(firestation);
    }

    public FirestationDTO getFirestationCoverage(int stationNumber) {
        List<String> addresses = firestationRepository.findAddressesByStationNumber(stationNumber);
        List<PersonCoveredDTO> coveredPersons = new ArrayList<>();
        int numberOfAdults = 0;

        for (String address : addresses) {
            List<PersonCoveredDTO> persons = firestationRepository.findPersonsByAddress(address);
            coveredPersons.addAll(persons);
            numberOfAdults += (int) persons.stream().filter(person -> personService.isAdult(person.firstName(), person.lastName())).count();
        }

        int numberOfChildren = coveredPersons.size() - numberOfAdults;

        return new FirestationDTO(coveredPersons, numberOfAdults, numberOfChildren);
    }

}