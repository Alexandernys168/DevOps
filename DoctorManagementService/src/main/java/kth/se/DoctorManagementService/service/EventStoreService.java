package kth.se.DoctorManagementService.service;

import kth.se.DoctorManagementService.model.Doctor;
import kth.se.DoctorManagementService.repository.EventStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class EventStoreService {


    @Autowired
    private EventStoreRepository eventStoreRepository;

    @KafkaListener(topics = "${doctor.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleLabResultEvent(Doctor event) throws ExecutionException, InterruptedException {
        eventStoreRepository.save(event);
    }


}
