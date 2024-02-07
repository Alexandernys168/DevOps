package kth.system.healthcare.PatientRegistrationService.service;

import kth.system.healthcare.PatientRegistrationService.model.Patient;
import kth.system.healthcare.PatientRegistrationService.model.PatientRegisteredEvent;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PatientRegistrationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String patientRegistrationTopic;

    @Autowired
    public PatientRegistrationService(KafkaTemplate<String, Object> kafkaTemplate,
                                      @Value("${patient.registration.topic}") String patientRegistrationTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.patientRegistrationTopic = patientRegistrationTopic;
    }

    public void registerPatient(Patient patient) {

      validatePatientData(patient);


      Long now = new Date().getTime();

        PatientRegisteredEvent event = new PatientRegisteredEvent(
                patient.id(),
                patient.name(),
                patient.dateOfBirth(),
                patient.email(),
                patient.phoneNumber(),
                now
        );

        kafkaTemplate.send(patientRegistrationTopic, event);
    }

    private void validatePatientData(Patient patient) {
        if (patient.name() == null || patient.name().isBlank()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
//        if (patient.dateOfBirth() == null || patient.dateOfBirth().isAfter(LocalDate.now())) {
//            throw new IllegalArgumentException("Valid patient date of birth is required.");
//        }
        if (!EmailValidator.getInstance().isValid(patient.email())) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }
}

