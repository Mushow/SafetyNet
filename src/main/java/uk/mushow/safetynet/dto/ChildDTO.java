package uk.mushow.safetynet.dto;

import java.util.List;

public record ChildDTO(String firstName, String lastName, int age, List<ChildFamilyDTO> householdMembers) {}