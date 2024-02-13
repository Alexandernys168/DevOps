package kth.se.LabResultService.service;

import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabResultService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String labResultTopic;
    private final EventStoreRepository eventStoreRepository;
   // private final NotificationService notificationService;

    @Autowired
    public LabResultService(KafkaTemplate<String, Object> kafkaTemplate,
                            EventStoreRepository eventStoreRepository,
                            @Value("${LabResult.topic}") String labResultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventStoreRepository = eventStoreRepository;
        this.labResultTopic = labResultTopic;
    }

    public void registerLabResult(LabResult labresult) {
        Long now = new Date().getTime();
        LabResult event = new LabResult(
                labresult.id(),
                labresult.patientId(),
                labresult.result(),
                now
        );
        kafkaTemplate.send(labResultTopic, event);

        //notificationService.sendNotification( "Lab result is available.");
    }

    public List<String> getAllEventsInStream(String streamName) {
        return eventStoreRepository.getAllEventsInStream(streamName);
    }


    public List<LabResult> getLabResultsByPatientId(String patientId) {
        List<LabResult> allLabResults = eventStoreRepository.getAllLabResults();

        return allLabResults.stream()
                .filter(result -> result.patientId().equals(patientId))
                .collect(Collectors.toList());
    }


}
