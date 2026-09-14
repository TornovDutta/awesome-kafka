package com.minikafka.core;

public class MessageRecord {
    public final Object message;
    public final long nextOffset;

    public MessageRecord(Object message, long nextOffset) {
        this.message = message;
        this.nextOffset = nextOffset;
    }
}
