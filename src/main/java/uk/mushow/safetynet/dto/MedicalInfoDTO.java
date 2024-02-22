package uk.mushow.safetynet.dto;

import java.util.List;

public record MedicalInfoDTO(List<String> medications, List<String> allergies) {}