# Topics in Awesome Kafka

## What is a Topic?
A Topic is a logical category or feed name where messages are published. It acts as the central storage unit for a specific stream of data.

## How it works in the background
Unlike an in-memory map or simple event bus, a Topic in Awesome Kafka is physically represented as an **append-only binary log file** on the disk.
When the application runs, it creates a directory named `minikafka-data/` in the project root. Inside this directory, each Topic gets its own folder containing a `.log` file.
For example, messages sent to `order-topic` are serialized and appended to `minikafka-data/order-topic/00000000.log`.

Because a Topic is a file on disk:
- **Durability:** Messages survive application restarts.
- **Order:** Messages are guaranteed to be stored in the exact order they were received.
- **Immutability:** Once a message is written to the topic log, it cannot be changed.
