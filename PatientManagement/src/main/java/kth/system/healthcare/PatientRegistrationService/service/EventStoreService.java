package kth.system.healthcare.PatientRegistrationService.service;

import kth.system.healthcare.PatientRegistrationService.model.PatientRegisteredEvent;
import kth.system.healthcare.PatientRegistrationService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class EventStoreService {

    @Autowired
    private EventStoreRepository eventStoreRepository;

    @KafkaListener(topics = "patient-registration-topic", groupId = "patient-registration-group")
    public void handlePatientRegisteredEvent(PatientRegisteredEvent event) throws ExecutionException, InterruptedException {
        eventStoreRepository.save(event);
    }

    @KafkaListener(topics = "labResult-topic", groupId = "patient-registration-group")
    public void handleLabResultEvent(String labResult) {
        String message = "Nytt labbresultat mottaget: " + labResult;
        notifyUser(message);

    }

    private void notifyUser(String message) {
        // Implementera logik för att skicka en notifiering till användaren, t.ex. via e-post, SMS, push-meddelande etc.
        System.out.println("Notifierar användaren: " + message);
    }

}
