package kth.system.healthcare.PatientRegistrationService.service;

import kth.system.healthcare.PatientRegistrationService.model.Patient;
import kth.system.healthcare.PatientRegistrationService.model.PatientRegisteredEvent;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PatientManagementService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String patientRegistrationTopic;
    private final List<PatientRegisteredEvent> allPatientEvents = new CopyOnWriteArrayList<>();

    @Autowired
    public PatientManagementService(KafkaTemplate<String, Object> kafkaTemplate,
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
                patient.phoneNumber()
                //now
        );

        kafkaTemplate.send(patientRegistrationTopic, event);
    }

    @KafkaListener(topics = "${patient.registration.topic}", groupId = "${spring.kafka.consumer.group-id-getevents}")
    public void consumeAllPatientEvents(ConsumerRecord<String, PatientRegisteredEvent> record) {
        PatientRegisteredEvent event = record.value(); //value är data payload, resten är metadata
        allPatientEvents.add(event);
    }

    public List<PatientRegisteredEvent> getAllPatientEvents() {
        return Collections.unmodifiableList(allPatientEvents);
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

