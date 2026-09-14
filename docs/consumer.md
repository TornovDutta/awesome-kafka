# Consumers in Awesome Kafka

## What is a Consumer?
A Consumer is the component that reads messages from a specific Topic. In Awesome Kafka, consumers are designed to read independently from the topics without interfering with producers or other consumers.

## How it works in the background
When a consumer subscribes to a topic, the `MiniKafkaBroker` spawns a dedicated background thread specifically for that consumer. 
This thread operates in a continuous polling loop. It opens the topic's log file on disk and reads messages sequentially. When it runs out of new messages, the thread briefly sleeps and checks again, ensuring asynchronous and continuous message processing.

## Implementation in Client Codebase
Consumers are created effortlessly using the `@MiniKafkaListener` annotation on any Spring Bean method.

```java
import com.minikafka.annotation.MiniKafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    // The broker will spawn a polling thread to feed messages to this method
    @MiniKafkaListener(topic = "order-topic")
    public void handleOrderEvent(String message) {
        System.out.println("Received: " + message);
    }
}
```
