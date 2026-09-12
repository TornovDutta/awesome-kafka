# Apache Kafka Architecture

Apache Kafka is a distributed event streaming platform capable of handling trillions of events a day. It provides a unified, high-throughput, low-latency platform for handling real-time data feeds.

## Core Concepts

### 1. Topics and Partitions
- **Topics**: A topic is a category or feed name to which records are published. Topics in Kafka are always multi-subscriber.
- **Partitions**: Topics are broken down into a number of partitions. Partitions allow you to parallelize a topic by splitting the data in a particular topic across multiple brokers. Each partition is an ordered, immutable sequence of records that is continually appended to.

### 2. Brokers and Clusters
- **Broker**: A Kafka cluster consists of one or more servers (Kafka brokers). A broker receives messages from producers, assigns offsets to them, and commits the messages to storage on disk. It also services consumers, responding to fetch requests for partitions and responding with the messages that have been committed to disk.
- **Cluster**: A group of brokers working together is called a Kafka cluster.

### 3. Producers
Producers are client applications that publish (write) events to Kafka. Producers can choose which partition to write to, either by specifying a partition key or using a round-robin approach.

### 4. Consumers and Consumer Groups
- **Consumers**: Consumers are client applications that subscribe to (read and process) events.
- **Consumer Groups**: Consumers are logically grouped into consumer groups. Each partition in a topic is consumed by exactly one consumer within a consumer group, ensuring that each message is processed only once per group.

### 5. ZooKeeper / KRaft
- **ZooKeeper**: Historically, Kafka used Apache ZooKeeper to manage cluster metadata, coordinate broker elections, and store consumer offsets (in older versions).
- **KRaft (Kafka Raft)**: In newer versions, Kafka has transitioned to using a self-managed metadata quorum (KRaft) to replace ZooKeeper, simplifying deployment and improving scalability.

## Data Flow Architecture

1. **Publishing**: The Producer sends records to a Kafka topic.
2. **Storage**: The Kafka Broker receives the records, appends them to a partition log, and stores them on disk for a configurable retention period.
3. **Consuming**: The Consumer polls the Kafka Broker for new records from the subscribed topics.
4. **Offset Management**: The Consumer tracks its position in the partition log using offsets, allowing it to resume consumption from where it left off in case of failure or restart.

## High Availability and Replication

Kafka provides high availability through replication. Each partition has one "leader" broker and zero or more "follower" brokers.
- The leader handles all read and write requests for the partition.
- Followers passively replicate the leader's data. If the leader fails, one of the in-sync followers will automatically become the new leader.
