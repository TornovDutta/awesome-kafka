# Mini Kafka Spring Boot Starter

A lightweight, in-memory event broker that simulates Kafka's core concepts (publish/subscribe to topics) without requiring any external infrastructure. 

This project is built as a **Spring Boot Starter**. Other users can include it in their `pom.xml` and start using it immediately without any extra configuration.

## Features
- **In-memory Broker:** No need to install or configure Zookeeper or a Kafka cluster.
- **Asynchronous Processing:** Messages are handled asynchronously by a cached thread pool.
- **Spring Boot Auto-configuration:** Automatically configures the broker, template, and listeners.

## How to Use

### 1. Add Dependency
Include this starter project in your target Spring Boot application's `pom.xml`:

```xml
<dependency>
    <groupId>com.minikafka</groupId>
    <artifactId>mini-kafka-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
*(Make sure to run `mvn clean install` on this library first to install it in your local `.m2` repository).*

### 2. Produce Messages
Inject `MiniKafkaTemplate` into your services to send messages to a specific topic.

```java
import com.minikafka.core.MiniKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private MiniKafkaTemplate kafkaTemplate;

    public void createOrder(String orderDetails) {
        // Business logic...
        System.out.println("Order created: " + orderDetails);
        
        // Publish an event to the "order-topic"
        kafkaTemplate.send("order-topic", orderDetails);
    }
}
```

### 3. Consume Messages
Use the `@MiniKafkaListener` annotation on any method within a Spring Bean to listen to a topic.

```java
import com.minikafka.annotation.MiniKafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    @MiniKafkaListener(topic = "order-topic")
    public void handleOrderEvent(String message) {
        System.out.println("Received order event: " + message);
        // Send notification...
    }
}
```

That's it! When `OrderService` sends a message, `NotificationService` will receive it asynchronously.
