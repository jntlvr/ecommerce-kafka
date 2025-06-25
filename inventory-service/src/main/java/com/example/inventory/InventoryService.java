package com.example.inventory;

import com.example.common.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class InventoryService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Set<String> processedOrders = new HashSet<>();

    public InventoryService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "orders", groupId = "inventory-group")
    public void processOrder(Order order) { // Desserialização direta para Order
        System.out.println("🎯 RECEBI ORDER NO INVENTORY! ID: " + order.getOrderId());
        if (processedOrders.contains(order.getOrderId())) {
            return; // idempotência
        }

        processedOrders.add(order.getOrderId());

        String result = (order.getItems().size() < 5) ? "SUCCESS" : "FAILED";
        String message = String.format("Order: %s | Items: %d | Status: %s",
                order.getOrderId(), order.getItems().size(), result);

        kafkaTemplate.send("inventory-events", order.getOrderId(), message);
    }
}