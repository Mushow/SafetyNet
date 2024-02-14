package uk.mushow.safetynet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.mushow.safetynet.dto.*;
import uk.mushow.safetynet.exception.NotFoundException;
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

    @GetMapping("childAlert")
    public ResponseEntity<List<ChildDTO>> getChildAlert(@RequestParam("address") String address) {
        try {
            List<ChildDTO> childAlerts = personService.getChildAlertByAddress(address);
            return ResponseEntity.ok(childAlerts);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("phoneAlert")
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam("firestation") int stationNumber) {
        try {
            PhoneAlertDTO phoneAlerts = firestationService.getPhoneAlertByStationNumber(stationNumber);
            return ResponseEntity.ok(phoneAlerts);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("fire")
    public ResponseEntity<List<ResidentDTO>> fire(@RequestParam("address") String address) {
        try {
            List<ResidentDTO> residentList = personService.getFireAlertByAddress(address);
            return ResponseEntity.ok(residentList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("flood/stations")
    public ResponseEntity<List<FloodDTO>> getFloodAlertByStations(@RequestParam List<Integer> stations) {
        try {
            List<FloodDTO> floodList = personService.getFloodAlertByStations(stations);
            return ResponseEntity.ok(floodList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        try {
            List<PersonInfoDTO> personInfoList = personService.getPersonInfo(firstName, lastName);
            return ResponseEntity.ok(personInfoList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("communityEmail")
    public ResponseEntity<List<String>> getCommunityEmail(@RequestParam("city") String city) {
        try {
            List<String> emails = personService.getCommunityEmail(city);
            return ResponseEntity.ok(emails);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
