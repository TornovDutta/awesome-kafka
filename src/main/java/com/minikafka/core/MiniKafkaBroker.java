package com.minikafka.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MiniKafkaBroker {
    private final Map<String, List<MessageListener>> listeners = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void publish(String topic, Object message) {
        List<MessageListener> topicListeners = listeners.get(topic);
        if (topicListeners != null) {
            for (MessageListener listener : topicListeners) {
                // Deliver messages asynchronously
                executorService.submit(() -> listener.onMessage(message));
            }
        }
    }

    public void subscribe(String topic, MessageListener listener) {
        listeners.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public interface MessageListener {
        void onMessage(Object message);
    }
}
