package kth.se.LabResultService.repository;

import com.eventstore.dbclient.*;
import kth.se.LabResultService.model.LabResult;
import kth.se.LabResultService.model.LabResultEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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


    public void save(LabResultEvent event) throws ExecutionException, InterruptedException {
        EventData eventData = EventData.builderAsJson("LabResultEvent", event).build();
        eventStoreDBClient.appendToStream(streamName, eventData).get();
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
