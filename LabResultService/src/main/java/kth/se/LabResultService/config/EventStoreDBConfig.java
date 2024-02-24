package kth.se.LabResultService.config;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {
    @Value("${eventstoredb.connection-string}")
    private String eventStoreDBConnectionString;

    @Bean
    public EventStoreDBClient eventStoreDBClient() {
        EventStoreDBClientSettings settings = EventStoreDBConnectionString.parseOrThrow(eventStoreDBConnectionString);
        EventStoreDBClient client = EventStoreDBClient.create(settings);
        return client;
    }






}
