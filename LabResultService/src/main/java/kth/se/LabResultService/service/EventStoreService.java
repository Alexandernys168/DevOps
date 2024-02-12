package kth.se.LabResultService.service;

import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.model.LabResultEvent;
import kth.se.LabResultService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class EventStoreService {


    @Autowired
    private EventStoreRepository eventStoreRepository;

    @KafkaListener(topics = "${LabResult.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleLabResultEvent(LabResultEvent event) throws ExecutionException, InterruptedException {
        eventStoreRepository.save(event);
    }


}
