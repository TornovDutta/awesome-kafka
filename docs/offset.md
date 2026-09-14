# Offsets in Awesome Kafka

## What is an Offset?
An Offset is a pointer or position marker that a Consumer uses to track exactly where it currently is within a Topic.

## How it works in the background
Because a Topic is a persistent log file on disk, messages are not deleted once they are read. Instead, the consumer keeps track of the **exact byte position** (the offset) of the next message it needs to read.
When the consumer's background polling thread successfully reads and processes a message, it updates its local offset to point to the start of the next message in the file.

### Replayability
Currently, every time a new consumer thread starts (e.g., when the application starts up), its offset begins at `0` (the very beginning of the file). This means the consumer will read and "replay" all historical messages stored in the Topic log before processing new live messages.
