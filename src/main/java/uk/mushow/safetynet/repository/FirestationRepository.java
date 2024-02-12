package uk.mushow.safetynet.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.dto.PersonCoveredDTO;
import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.model.Person;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FirestationRepository {

    @Autowired
    private DataWrapper dataWrapper;

    public FirestationRepository(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public void create(Firestation firestation) {
        dataWrapper.getFirestations().add(firestation);
    }

    public void update(Firestation firestation) throws StationNotFoundException {
        boolean wasUpdated = false;
        for (Firestation currentFirestation : dataWrapper.getFirestations()) {
            if (currentFirestation.getAddress().equals(firestation.getAddress())) {
                currentFirestation.setStation(firestation.getStation());
                wasUpdated = true;
            }
        }

        if (!wasUpdated) {
            throw new StationNotFoundException("The station at " + firestation.getAddress() +
                    " was not found!");
        }
    }

    public void delete(Firestation firestation) throws StationNotFoundException {
        boolean wasRemoved = dataWrapper.getFirestations().
                removeIf(f -> f.getAddress().equals(firestation.getAddress()) &&
                         f.getStation() == firestation.getStation());

        if (!wasRemoved) {
            throw new StationNotFoundException("The station at " + firestation.getAddress() +
                    " numbered " + firestation.getStation() + " was not found!");
        }
    }

    public List<String> findAddressesByStationNumber(int stationNumber) {
        return dataWrapper.getFirestations().stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .collect(Collectors.toList());
    }

    public List<PersonCoveredDTO> findPersonsByAddress(String address) {
        return dataWrapper.getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .map(p -> new PersonCoveredDTO(p.getFirstName(), p.getLastName(), p.getAddress(), p.getPhone()))
                .collect(Collectors.toList());
    }

    public List<String> findPhoneNumbersByAddress(String address) {
        return dataWrapper.getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .map(Person::getPhone)
                .collect(Collectors.toList());
    }
}
