package uk.mushow.safetynet.repository;

import org.springframework.stereotype.Repository;
import uk.mushow.safetynet.data.DataWrapper;
import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.Firestation;

@Repository
public class FirestationRepository {

    private final DataWrapper dataWrapper;

    public FirestationRepository(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public void create(Firestation firestation) {
        dataWrapper.firestations().add(firestation);
    }

    public void update(Firestation firestation) throws StationNotFoundException {
        boolean wasUpdated = false;
        for (Firestation currentFirestation : dataWrapper.firestations()) {
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
        boolean wasRemoved = dataWrapper.firestations().
                removeIf(f -> f.getAddress().equals(firestation.getAddress()) &&
                         f.getStation().equals(firestation.getStation()));

        if (!wasRemoved) {
            throw new StationNotFoundException("The station at " + firestation.getAddress() +
                    " numbered " + firestation.getStation() + " was not found!");
        }
    }


}
