package com.minikafka.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MiniKafkaBroker {
    // Map to hold persistent topic logs rather than in-memory listeners
    private final Map<String, TopicLog> topics = new ConcurrentHashMap<>();
    
    // Thread pool to handle consumer polling
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private TopicLog getTopic(String topicName) {
        return topics.computeIfAbsent(topicName, TopicLog::new);
    }

    public void publish(String topic, Object message) {
        // Append message to the persistent log file on disk
        getTopic(topic).append(message);
    }

    public void subscribe(String topic, MessageListener listener) {
        TopicLog topicLog = getTopic(topic);
        
        // Start a dedicated polling thread for this consumer
        executorService.submit(() -> {
            long currentOffset = 0; // Starts from the beginning of the log (offset 0)
            
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Try to read a message at the current byte offset
                    MessageRecord record = topicLog.read(currentOffset);
                    
                    if (record != null) {
                        // Message found: Deliver it and advance the offset
                        listener.onMessage(record.message);
                        currentOffset = record.nextOffset;
                    } else {
                        // Reached end of file: wait 100ms before polling again
                        Thread.sleep(100); 
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Error reading from topic " + topic + ": " + e.getMessage());
                    try { 
                        Thread.sleep(1000); 
                    } catch (InterruptedException ie) { 
                        Thread.currentThread().interrupt(); 
                        break; 
                    }
                }
            }
        });
    }

    public interface MessageListener {
        void onMessage(Object message);
    }
}
