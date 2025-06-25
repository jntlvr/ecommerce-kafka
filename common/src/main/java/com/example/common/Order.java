package com.example.common;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {
    private String orderId;
    private Instant timestamp;
    private List<String> items;

    // Construtor padrão vazio para Jackson
    public Order() {}

    // Construtor original para criação de novo pedido
    public Order(List<String> items) {
        this.orderId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.items = items;
    }

    // getters e setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
