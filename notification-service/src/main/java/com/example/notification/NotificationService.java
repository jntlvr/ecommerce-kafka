package com.example.notification;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "inventory-events", groupId = "notification-group")
    public void listen(String message) {
        System.out.println("[NOTIFICATION] " + message);
    }
}