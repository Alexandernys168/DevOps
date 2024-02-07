package kth.system.healthcare.PatientRegistrationService.repository;


import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import kth.system.healthcare.PatientRegistrationService.model.PatientRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class EventStoreRepository {

    private final EventStoreDBClient eventStoreDBClient;
    private final String streamName;

    @Autowired
    public EventStoreRepository(EventStoreDBClient eventStoreDBClient,
                                @Value("${eventstoredb.stream.name}") String streamName) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.streamName = streamName;
    }


    public void save(PatientRegisteredEvent event) throws ExecutionException, InterruptedException {
        EventData eventData = EventData.builderAsJson("PatientRegisteredEvent", event).build();
        eventStoreDBClient.appendToStream(streamName, eventData).get();
    }

}