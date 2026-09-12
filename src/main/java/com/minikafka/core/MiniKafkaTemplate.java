package com.minikafka.core;

public class MiniKafkaTemplate {
    private final MiniKafkaBroker broker;

    public MiniKafkaTemplate(MiniKafkaBroker broker) {
        this.broker = broker;
    }

    public void send(String topic, Object message) {
        broker.publish(topic, message);
    }
}
