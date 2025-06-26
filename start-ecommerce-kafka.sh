#!/bin/bash

echo "🚀 Iniciando Apache Kafka e Zookeeper..."
# Ajuste os caminhos abaixo conforme a instalação do Kafka
KAFKA_DIR=~/kafka  # Altere se necessário
cd $KAFKA_DIR

# Inicia Zookeeper
gnome-terminal -- bash -c "./bin/zookeeper-server-start.sh config/zookeeper.properties; exec bash"

# Aguarda alguns segundos antes de subir o Kafka
sleep 5

# Inicia Kafka
gnome-terminal -- bash -c "./bin/kafka-server-start.sh config/server.properties; exec bash"

sleep 5
cd -

echo "🧩 Iniciando serviços Spring Boot..."
cd ecommerce-kafka/order-service
gnome-terminal -- bash -c "./mvnw spring-boot:run; exec bash"
cd ../inventory-service
gnome-terminal -- bash -c "./mvnw spring-boot:run; exec bash"
cd ../notification-service
gnome-terminal -- bash -c "./mvnw spring-boot:run; exec bash"
cd ../..
