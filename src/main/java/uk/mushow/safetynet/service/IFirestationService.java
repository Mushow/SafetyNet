package uk.mushow.safetynet.service;

import uk.mushow.safetynet.model.Firestation;

public interface IFirestationService {

    void addFirestation(Firestation firestation);

    void updateFirestation(Firestation firestation);

    void deleteFirestation(Firestation firestation);

}
