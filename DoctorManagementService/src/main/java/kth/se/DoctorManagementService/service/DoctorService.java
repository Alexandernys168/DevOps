package kth.se.DoctorManagementService.service;

import kth.se.DoctorManagementService.model.AccessRequest;
import kth.se.DoctorManagementService.model.AccessResponse;
import kth.se.DoctorManagementService.model.Doctor;
import kth.se.DoctorManagementService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String labResultTopic;
    private final EventStoreRepository eventStoreRepository;
   // private final NotificationService notificationService;

    @Autowired
    private KafkaTemplate<String, AccessResponse> kafkaTemplateAccess;

    @Autowired
    public DoctorService(KafkaTemplate<String, Object> kafkaTemplate,
                         EventStoreRepository eventStoreRepository,
                         @Value("${LabResult.topic}") String labResultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventStoreRepository = eventStoreRepository;
        this.labResultTopic = labResultTopic;
    }

    private final String accessResponseTopic = "access-response-topic";

    /*
    public void processAccessRequest(AccessRequest accessRequest) {
        AccessResponse accessResponse = new AccessResponse(accessRequest.getId(), accessRequest.getDoctorId(), accessGranted);
        kafkaTemplateAccess.send(accessResponseTopic, accessResponse);
    }
    */
    public void registerLabResult(Doctor labresult) {
        //LocalDateTime now = LocalDateTime.now();
       Doctor event = new Doctor(
                labresult.id(),
                labresult.name(),
                labresult.lastName()
        );
        kafkaTemplate.send(labResultTopic, event);


    }

    public List<String> getAllEventsInStream(String streamName) {
        return eventStoreRepository.getAllEventsInStream(streamName);
    }



}
