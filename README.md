# Sistema Distribuído de E-commerce com Apache Kafka

## Visão Geral

Este projeto representa uma simulação de um sistema distribuído de e-commerce baseado em microsserviços, onde a comunicação entre serviços ocorre de forma assíncrona utilizando o Apache Kafka. O fluxo de eventos envolve a criação de pedidos, a verificação de estoque e o envio de notificações simuladas.

## Arquitetura dos Serviços

- **Order-Service**: Serviço responsável por receber pedidos via API REST e publicá-los no tópico `orders` do Kafka.
- **Inventory-Service**: Consome mensagens do tópico `orders`, verifica o estoque e publica o resultado no tópico `inventory-events`.
- **Notification-Service**: Monitora o tópico `inventory-events` e realiza simulações de notificações (como e-mails ou SMS) via registros no console.

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Apache Kafka (instância local)
- Maven
- Docker e Docker Compose (opcional)

---

## Requisitos Funcionais

| Código | Descrição |
|--------|-----------|
| RF-1   | Criar tópicos/fila orders e inventory-events via kafka-topics.sh |
| RF-2   | Order‐Service expõe uma REST API (POST /orders) que gera um UUID, timestamp e lista de itens. |
| RF-3   | Inventory‐Service processa mensagens em ordem e publica sucesso ou falha (sem estoque). |
| RF-4   | Notification‐Service registra no console a notificação enviada. |

## Requisitos Não Funcionais

### 1. Escalabilidade

O sistema permite escalar horizontalmente os serviços consumidores:

- Aumentando o número de partições em cada tópico Kafka, é possível distribuir a carga entre múltiplas instâncias.
- Os serviços, ao pertencerem ao mesmo grupo de consumidores, recebem eventos de forma balanceada automaticamente.

### 2. Tolerância a Falhas

- O Kafka armazena mensagens até que os consumidores confirmem o processamento.
- Em caso de falha de algum serviço, os eventos permanecem no tópico até que o serviço retorne.
- Se configurado em cluster, o Kafka pode replicar dados entre brokers, garantindo alta disponibilidade.

### 3. Idempotência

Para evitar efeitos colaterais no reprocessamento de mensagens:

- Cada pedido possui um identificador único (UUID) que pode ser usado para verificar duplicidade.
- A camada de persistência pode usar restrições de unicidade.
- O Kafka Producer pode ser configurado com `enable.idempotence=true` para evitar reenvio duplicado.

---

## Instruções de Instalação

### 1. Pré-requisitos

- Java 21
- Apache Kafka instalado e configurado localmente
- Docker (opcional, para PostgreSQL e Kafka)
- Maven

### 2. Inicialização do Kafka

Caso esteja usando Kafka instalado manualmente:

```bash
# Iniciar Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Iniciar Kafka
bin/kafka-server-start.sh config/server.properties
```

### 3. Criação dos Tópicos

```bash
bin/kafka-topics.sh --create --topic orders --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

bin/kafka-topics.sh --create --topic inventory-events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

### 4. Compilar e Executar os Serviços

Compile o projeto base:

```bash
cd ecommerce-kafka
mvn clean install
```

Depois, rode cada serviço individualmente:

```bash
# Order Service
cd order-service
mvn spring-boot:run
```

```bash
# Inventory Service
cd inventory-service
mvn spring-boot:run
```

```bash
# Notification Service
cd notification-service
mvn spring-boot:run
```

---

## Testes

Você pode testar o sistema utilizando um cliente HTTP como Postman, Insomnia ou curl.

### Requisição de Exemplo

```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

A partir dessa solicitação, o fluxo será:

1. Order-Service publica no Kafka.
2. Inventory-Service processa o pedido e publica o status.
3. Notification-Service imprime a notificação no console.

---

## Observações

- O projeto pode ser adaptado para uso com banco de dados relacional (PostgreSQL), mas essa configuração não está incluída por padrão.
- Para fins de visualização do Kafka, ferramentas como Kafka Tool ou Kafdrop podem ser utilizadas.

---


