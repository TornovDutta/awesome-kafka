# Producers in Awesome Kafka

## What is a Producer?
A Producer is the component responsible for publishing (or sending) messages to a specific category or feed, known as a Topic. 

## How it works in the background
When a producer publishes a message using the `MiniKafkaBroker`, it doesn't wait for consumers to receive it. Instead, the producer serializes the message into raw bytes and appends it directly to a persistent log file on disk (e.g., `minikafka-data/<topic-name>/00000000.log`). Because it writes straight to an append-only file, the producer is completely decoupled from consumer speeds and failures.

## Implementation in Client Codebase
Developers using this Spring Boot Starter can produce messages by injecting the auto-configured `MiniKafkaTemplate` into their service classes.

```java
import com.minikafka.core.MiniKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private MiniKafkaTemplate kafkaTemplate;

    public void createOrder(String orderDetails) {
        // Appends the message to the topic's disk log immediately
        kafkaTemplate.send("order-topic", orderDetails);
    }
}
```
