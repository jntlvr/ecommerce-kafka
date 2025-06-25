package com.example.order;

import com.example.common.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.*;

@SpringBootApplication
@RestController
public class OrderServiceApplication {
    private final KafkaProducer<String, String> producer;
    private final ObjectMapper mapper = new ObjectMapper();

    public OrderServiceApplication() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.producer = new KafkaProducer<>(props);

        mapper.registerModule(new JavaTimeModule());
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody List<String> items) throws Exception {
        System.out.println("=== RECEBIDO POST /orders ==="); // ✅ Log de entrada
        Order order = new Order(items);
        System.out.println("📦 Order criada: ID=" + order.getOrderId() + ", Itens=" + items.size());

        String orderJson = mapper.writeValueAsString(order);
        System.out.println("📤 JSON preparado: " + orderJson); // ✅ Verifique o JSON

        producer.send(new ProducerRecord<>("orders", order.getOrderId(), orderJson), (metadata, exception) -> {
            if (exception != null) {
                System.err.println("❌ Falha ao enviar: " + exception.getMessage());
            } else {
                System.out.println("🚀 Enviado para Kafka! Tópico: " + metadata.topic() +
                        ", Partição: " + metadata.partition());
            }
        }).get(); // ⚠️ Remova o .get() em produção (está bloqueando)

        return ResponseEntity.ok("Order sent: " + order.getOrderId());
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}