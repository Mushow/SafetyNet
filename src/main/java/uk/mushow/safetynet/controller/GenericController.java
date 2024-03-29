package uk.mushow.safetynet.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.mushow.safetynet.dto.*;
import uk.mushow.safetynet.exception.NotFoundException;
import uk.mushow.safetynet.service.FirestationService;
import uk.mushow.safetynet.service.PersonService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/")
public class GenericController {

    private final PersonService personService;
    private final FirestationService firestationService;

    GenericController(PersonService personService, FirestationService firestationService) {
        this.personService = personService;
        this.firestationService = firestationService;
    }

    @GetMapping("childAlert")
    public ResponseEntity<List<ChildDTO>> getChildAlert(@RequestParam("address") String address) throws NotFoundException {
        List<ChildDTO> childAlerts = personService.getChildAlertByAddress(address);
        return ResponseEntity.ok(childAlerts);
    }

    @GetMapping("phoneAlert")
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam("firestation") int stationNumber) throws NotFoundException {
        PhoneAlertDTO phoneAlerts = firestationService.getPhoneAlertByStationNumber(stationNumber);
        return ResponseEntity.ok(phoneAlerts);
    }

    @GetMapping("fire")
    public ResponseEntity<List<ResidentDTO>> fire(@RequestParam("address") String address) throws NotFoundException {
        List<ResidentDTO> residentList = personService.getFireAlertByAddress(address);
        return ResponseEntity.ok(residentList);
    }

    @GetMapping("flood/stations")
    public ResponseEntity<List<FloodDTO>> getFloodAlertByStations(@RequestParam List<Integer> stations) throws NotFoundException {
        List<FloodDTO> floodList = personService.getFloodAlertByStations(stations);
        return ResponseEntity.ok(floodList);
    }

    @GetMapping("personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) throws NotFoundException {
        List<PersonInfoDTO> personInfoList = personService.getPersonInfo(firstName, lastName);
        return ResponseEntity.ok(personInfoList);
    }

    @GetMapping("communityEmail")
    public ResponseEntity<List<String>> getCommunityEmail(@RequestParam("city") String city) throws NotFoundException {
        List<String> emails = personService.getCommunityEmail(city);
        return ResponseEntity.ok(emails);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

}
