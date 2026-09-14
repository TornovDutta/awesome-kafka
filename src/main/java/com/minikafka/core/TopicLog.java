package com.minikafka.core;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TopicLog {
    private final Path filePath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public TopicLog(String topicName) {
        // Store topic logs inside a 'minikafka-data' directory in the project root
        Path dir = Paths.get("minikafka-data", topicName);
        this.filePath = dir.resolve("00000000.log");
        try {
            Files.createDirectories(dir);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create topic log for " + topicName, e);
        }
    }

    public void append(Object message) {
        lock.writeLock().lock();
        try {
            byte[] data = serialize(message);
            // Append length-prefixed message to the file
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath.toFile(), true))) {
                dos.writeInt(data.length);
                dos.write(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to append message to topic", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public MessageRecord read(long offset) {
        lock.readLock().lock();
        try (RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "r")) {
            if (offset >= raf.length()) {
                return null; // No new message
            }
            raf.seek(offset);
            int length = raf.readInt();
            byte[] data = new byte[length];
            raf.readFully(data);
            Object msg = deserialize(data);
            // Return message and the exact byte offset for the next message
            return new MessageRecord(msg, offset + 4 + length); 
        } catch (Exception e) {
            throw new RuntimeException("Failed to read message at offset " + offset, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
        return bos.toByteArray();
    }

    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }
}
