package kth.se.LabResultService.config;


import com.google.gson.Gson;
import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.service.LabResultService;
import kth.se.LabResultService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LabResultConsumer {

    private final Gson gson = new Gson();

    @Autowired
    private LabResultService labResultEventStoreService;
    @Autowired
    private NotificationService notificationService;
    @KafkaListener(topics = "lab-result-topic", groupId = "lab-result-group")
    public void consumeLabResult(String labResultJson) {
        try {
            // Convert JSON string to LabResult object
            LabResult labResult = gson.fromJson(labResultJson, LabResult.class);

            // Save lab result to EventStoreDB
            labResultEventStoreService.saveLabResult(labResult);
            // Skicka notis till användare
            notificationService.sendNotificationToUser(labResult.getPatientId(), "Ditt labbprov har sparats och är tillgängligt");
            System.out.println("Lab result consumed and saved: " + labResult);
        } catch (Exception e) {
            System.err.println("Error consuming lab result: " + e.getMessage());
        }
    }

}
