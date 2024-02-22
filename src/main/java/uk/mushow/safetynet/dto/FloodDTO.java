package uk.mushow.safetynet.dto;

import java.util.List;
import java.util.Map;

public record FloodDTO(Map<String, List<ResidentDTO>> householdsByAddress) {}