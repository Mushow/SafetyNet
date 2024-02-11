package uk.mushow.safetynet.dto;

import java.util.List;

public record FirestationDTO (List<PersonCoveredDTO> personsCovered, int numberOfAdults, int numberOfChildren) {}