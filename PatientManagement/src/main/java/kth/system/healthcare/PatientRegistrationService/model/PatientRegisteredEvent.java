package kth.system.healthcare.PatientRegistrationService.model;

import java.util.Date;

public record PatientRegisteredEvent(String id, String name, Date dateOfBirth,
                                     String email, String phoneNumber , Long registeredAt ) {}
