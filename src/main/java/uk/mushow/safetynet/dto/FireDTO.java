package uk.mushow.safetynet.dto;

import java.util.List;

public record FireDTO(List<ResidentDTO> residents, int stationNumber) {}