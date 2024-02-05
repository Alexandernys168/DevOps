package kth.se.LabResultService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);
    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void receiveNotification(String message) {
        logger.info("Received notification: {}", message);
    }


}
