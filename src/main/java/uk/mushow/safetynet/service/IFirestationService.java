package uk.mushow.safetynet.service;

import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.model.Firestation;

public interface IFirestationService {

    void addFirestation(Firestation firestation);

    void updateFirestation(Firestation firestation) throws NotFoundException;

    void deleteFirestation(Firestation firestation) throws NotFoundException;

}
