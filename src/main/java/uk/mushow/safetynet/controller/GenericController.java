package uk.mushow.safetynet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.mushow.safetynet.dto.ChildDTO;
import uk.mushow.safetynet.service.FirestationService;
import uk.mushow.safetynet.service.PersonService;

@RestController
@RequestMapping("/")
public class GenericController {

    private final PersonService personService;
    GenericController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/childAlert")
    public ChildDTO getChildAlert(@RequestParam("address") String address) {
        return personService.getChildAlertByAddress(address);
    }

}
