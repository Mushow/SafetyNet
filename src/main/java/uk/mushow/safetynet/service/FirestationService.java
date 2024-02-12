package uk.mushow.safetynet.service;

import org.springframework.stereotype.Service;
import uk.mushow.safetynet.exception.StationNotFoundException;
import uk.mushow.safetynet.model.Firestation;
import uk.mushow.safetynet.repository.FirestationRepository;

@Service
public class FirestationService implements IFirestationService {

    private final FirestationRepository firestationRepository;

    public FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
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


}


