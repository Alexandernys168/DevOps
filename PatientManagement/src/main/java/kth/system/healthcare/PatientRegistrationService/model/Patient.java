package kth.system.healthcare.PatientRegistrationService.model;

import java.util.Date;

public record Patient(String id, String name, Date dateOfBirth, String email, String phoneNumber) {
}
