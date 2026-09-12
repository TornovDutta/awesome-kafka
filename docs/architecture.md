# Awesome Kafka (Mini Kafka) Architecture

Awesome Kafka is a lightweight, in-memory event broker designed as a Spring Boot Starter. It simulates the core concepts of publish/subscribe messaging without the need for external infrastructure like ZooKeeper, KRaft, or a real Kafka cluster.

## Architecture

The project is built around a few core components that handle message routing, asynchronous processing, and seamless Spring Boot integration:

### 1. MiniKafkaBroker
The `MiniKafkaBroker` is the central hub of the system. It maintains an in-memory registry mapping topics to their respective subscribers using a thread-safe `ConcurrentHashMap`. When a message is published, the broker routes it to all subscribed listeners. 

To ensure non-blocking operations, it utilizes an `ExecutorService` (a cached thread pool) to deliver messages asynchronously to each subscriber.

### 2. MiniKafkaTemplate
`MiniKafkaTemplate` provides a simple, high-level API for producing messages. Modeled after Spring's `KafkaTemplate`, developers can inject this class into their services and use the `send(topic, message)` method to publish events seamlessly.

### 3. @MiniKafkaListener
Consumers are defined using the `@MiniKafkaListener` annotation. By annotating a method within a Spring Bean and specifying a topic, the method becomes an event subscriber. 

Under the hood, a custom `BeanPostProcessor` (`MiniKafkaListenerAnnotationBeanPostProcessor`) scans the application context during startup, identifies these annotated methods, and registers them as listeners directly with the `MiniKafkaBroker`.

## Features

Awesome Kafka currently provides the following features out-of-the-box:

- **Zero Infrastructure Required**: An entirely in-memory broker that removes the need to install or manage an actual Kafka cluster.
- **Asynchronous Processing**: Messages are delivered and processed asynchronously using a cached thread pool, ensuring that publishers are not blocked by slow consumers.
- **Spring Boot Auto-Configuration**: Automatically configures and wires up the broker, template, and listener post-processor. Just include the dependency, and it works immediately without any manual configuration.
- **Thread-Safe Routing**: Safely handles concurrent publishing and subscribing using `ConcurrentHashMap` and `CopyOnWriteArrayList`.
- **Familiar API**: Provides an API structure (`MiniKafkaTemplate` and `@MiniKafkaListener`) that closely mirrors actual Spring Kafka, making it an excellent tool for local development, testing, or lightweight event-driven applications.
