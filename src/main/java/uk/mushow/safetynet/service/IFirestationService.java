package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.Firestation;

public interface IFirestationService {

    void addFirestation(Firestation firestation);

    void updateFirestation(Firestation firestation) throws StationNotFoundException;

    void deleteFirestation(Firestation firestation) throws StationNotFoundException;

}
