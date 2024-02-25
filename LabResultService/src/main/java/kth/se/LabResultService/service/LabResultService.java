package kth.se.LabResultService.service;

import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LabResultService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String LabResultTopic;
    private final EventStoreRepository eventStoreRepository;


    @Autowired
    public LabResultService(KafkaTemplate<String, Object> kafkaTemplate,
                            EventStoreRepository eventStoreRepository ,@Value("${LabResult.topic}") String LabResultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventStoreRepository=eventStoreRepository;
        this.LabResultTopic=LabResultTopic;
    }

    public void registerLabResult(LabResult labresult){
        Long now = new Date().getTime();
        LabResult event= new LabResult(
                labresult.id(),
                labresult.patientId(),
                labresult.result(),
                now
        );
        kafkaTemplate.send(LabResultTopic,event);
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

    public List<LabResult> getLabResultsById(String id) {
        List<LabResult> allLabResults = eventStoreRepository.getAllLabResults();

        return allLabResults.stream()
                .filter(result -> result.id().equals(id))
                .collect(Collectors.toList());
    }

}
