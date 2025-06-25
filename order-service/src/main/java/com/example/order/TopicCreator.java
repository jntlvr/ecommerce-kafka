package com.example.order;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;

public class TopicCreator {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient admin = AdminClient.create(config)) {
            admin.createTopics(Collections.singletonList(new NewTopic("orders", 1, (short) 1))).all().get();
            admin.createTopics(Collections.singletonList(new NewTopic("inventory-events", 1, (short) 1))).all().get();
            System.out.println("Tópicos criados com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar tópicos: " + e.getMessage());
        }
    }
}
