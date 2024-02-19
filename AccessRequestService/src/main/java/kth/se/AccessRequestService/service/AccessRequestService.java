package kth.se.AccessRequestService.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kth.se.AccessRequestService.model.AccessRequest;
import kth.se.AccessRequestService.model.AccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AccessRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AccessRequestService.class);
    @Autowired
    Gson gson;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${access.response.topic}")
    private String accessResponseTopic;


    @KafkaListener(topics = "${access.request.topic}", groupId = " accessRequest-group")
    public void receiveAccessRequest(String accessRequest)  {

        try {
            logger.info("Received access request: {}", accessRequest);

            AccessRequest request = objectMapper.readValue(accessRequest, AccessRequest.class);

            String doctorId = request.getDoctorId();
            String requestId = request.getRequestId();

            AccessResponse accessResponse = new AccessResponse( requestId,doctorId, true); // or false
            kafkaTemplate.send(accessResponseTopic, gson.toJson(accessResponse));
            logger.info("Access response sent: {}", accessResponse);

        } catch (IOException e) {
            logger.error("Error processing access request: {}", e.getMessage());
        }



    }





}