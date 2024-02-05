package kth.se.LabResultService.service;

import com.eventstore.dbclient.*;
import com.google.gson.*;
import kth.se.LabResultService.model.LabResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LabResultService {

    private final KafkaTemplate<String, LabResult> kafkaTemplate;
    private final EventStoreDBClient eventStoreDBClient;
    private static final String STREAM = "LabResult_Stream";
     private static final Gson gson = new Gson();

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
            String labResultJson = new Gson().toJson(labResult);

            EventData event = EventData
                    .builderAsJson("LabResult", labResultJson)
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
