package kth.se.LabResultService.service;

import com.eventstore.dbclient.*;

import com.google.gson.*;
import kth.se.LabResultService.model.LabResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LabResultService {

    private final KafkaTemplate<String, LabResult> kafkaTemplate;
    private final EventStoreDBClient eventStoreDBClient;
    private static final String STREAM = "LabResultStream";
     // private static final Gson gson = new Gson();

    @Autowired
    public LabResultService(KafkaTemplate<String, LabResult> kafkaTemplate, EventStoreDBClient eventStoreDBClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventStoreDBClient = eventStoreDBClient;
    }

    public void publishLabResult(LabResult labResult) {
        kafkaTemplate.send("lab-result-topic", labResult.getId(), labResult);
    }

    public void saveLabResult(LabResult labResult) {
        try {

            EventData event = EventData
                    .builderAsJson(UUID.randomUUID(), "LabResult", labResult)
                    .build();

            WriteResult writeResult = eventStoreDBClient
                    .appendToStream(STREAM, event)
                    .get();

            System.out.println("Lab result saved to EventStoreDB: " + labResult);
        } catch (Exception e) {
            System.err.println("Error saving lab result to EventStoreDB: " + e.getMessage());
        }
    }


    public List<String> getAllEventsInStream(String streamName) {
        List<String> events = new ArrayList<>();
        try {
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();

            ReadResult readResult = eventStoreDBClient.readStream(streamName, options).get();
            for (ResolvedEvent resolvedEvent : readResult.getEvents()) {
                String eventData = new String(resolvedEvent.getOriginalEvent().getEventData());

                events.add(eventData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }








}
