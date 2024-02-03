package kth.se.LabResultService.service;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.WriteResult;
import kth.se.LabResultService.model.LabResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;


@Service
public class LabResultService {

    private final KafkaTemplate<String, LabResult> kafkaTemplate;
    private final EventStoreDBClient eventStoreDBClient;
    private static final String STREAM = "LabResult_Stream";
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
            // Convert LabResult object to JSON string
            String labResultJson = new Gson().toJson(labResult);

            // Create EventData object with JSON data
            EventData event = EventData
                    .builderAsJson("LabResult", labResultJson)
                    .build();

            // Append event to the stream
            WriteResult writeResult = eventStoreDBClient
                    .appendToStream(STREAM, event)
                    .get();

            System.out.println("Lab result saved to EventStoreDB: " + labResult);
        } catch (Exception e) {
            System.err.println("Error saving lab result to EventStoreDB: " + e.getMessage());
        }
    }

    /*
    public List<String> getLabResultsFromStream(String stream) throws Exception {
        List<String> resultList = new ArrayList<>();
        ReadResult eventStream = eventStoreDBClient.readStream(stream, ReadStreamOptions.get().fromStart()).get();
        for (ResolvedEvent re : eventStream.getEvents()) {
            LabResult labResult = gson.fromJson(new String(re.getOriginalEvent().getEventData()), LabResult.class);
            resultList.add(String.format("ID: %s, Result: %s, Patient ID: %s", labResult.getId(), labResult.getResult(), labResult.getPatientId()));
        }
        return resultList;
    }

     */


}
