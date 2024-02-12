package uk.mushow.safetynet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.mushow.safetynet.dto.ChildDTO;
import uk.mushow.safetynet.dto.PhoneAlertDTO;
import uk.mushow.safetynet.dto.ResidentDTO;
import uk.mushow.safetynet.service.FirestationService;
import uk.mushow.safetynet.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/")
public class GenericController {

    private final PersonService personService;
    private final FirestationService firestationService;

    GenericController(PersonService personService, FirestationService firestationService) {
        this.personService = personService;
        this.firestationService = firestationService;
    }

    @GetMapping("/childAlert")
    public List<ChildDTO> getChildAlert(@RequestParam("address") String address) {
        return personService.getChildAlertByAddress(address);
    }

    @GetMapping("phoneAlert")
    public PhoneAlertDTO getPhoneAlert(@RequestParam("firestation") int stationNumber) {
        return firestationService.getPhoneAlertByStationNumber(stationNumber);
    }

    @GetMapping("fire")
    public List<ResidentDTO> fire(@RequestParam("address") String address) {
        return personService.getFireAlertByAddress(address);
    }

}
