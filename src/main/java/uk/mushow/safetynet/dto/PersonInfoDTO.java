package uk.mushow.safetynet.dto;

public record PersonInfoDTO(String lastName, String address, int age, String email, MedicalInfoDTO medicalInfo) {}