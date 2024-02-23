package kth.se.DoctorManagementService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kth.se.DoctorManagementService.model.AccessResponse;
import kth.se.DoctorManagementService.model.Doctor;
import kth.se.DoctorManagementService.repository.EventStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;
    // private final KafkaTemplate<String, Boolean> kafkaTemplateAccess;

    @Autowired
    private  EventStoreRepository eventStoreRepository;
    // private final NotificationService notificationService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${access.request.topic}")
    private  String accessRequestTopic ;
    @Value("${access.response.topic}")
    private String accessResponseTopic;
    @Value("${doctor.topic}")
    private String doctorTopic;

    @Autowired
    SimpMessagingTemplate messagingTemplate ;

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    ObjectMapper objMap =new ObjectMapper();


    @Autowired
    Gson gson;

    public void requestAccess(String accessRequest) {
        logger.info("Sending access request: {}", accessRequest);
        kafkaTemplate.send(accessRequestTopic, gson.toJson(accessRequest));

    }

    @KafkaListener(topics ="${access.response.topic}", groupId = "accessResponse-group")
    public void processAccessResponse(String accessResponse) {

        try {
            AccessResponse request = objectMapper.readValue(accessResponse, AccessResponse.class);
            String doctorId = request.getDoctorId();
            String requestId = request.getRequestId();
            Boolean accessGranted= request.isAccessGranted();

            logger.info("Received access response: {}", request );

            if(accessGranted){

                messagingTemplate.convertAndSend("/topic/access", "Access granted!");
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    public void registerDoctor(Doctor doctor) {
        Doctor event = new Doctor(
                doctor.id(),
                doctor.name(),
                doctor.lastName()
        );
        kafkaTemplate.send(doctorTopic, event);

    }

    public List<String> getAllEventsInStream(String streamName) {
        return eventStoreRepository.getAllEventsInStream(streamName);
    }




}
