package kth.se.LabResultService.service;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.ResolvedEvent;
import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.model.LabResultEvent;
import kth.se.LabResultService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class LabResultService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String LabResultTopic;
    private final EventStoreRepository eventStoreRepository;


    @Autowired
    public LabResultService(KafkaTemplate<String, Object> kafkaTemplate, EventStoreRepository eventStoreRepository ,@Value("${LabResult.topic}") String LabResultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventStoreRepository=eventStoreRepository;
        this.LabResultTopic=LabResultTopic;
    }

    public void registerLabResult(LabResult labresult){
        LocalDateTime now = LocalDateTime.now();
        LabResultEvent event= new LabResultEvent(
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



}
