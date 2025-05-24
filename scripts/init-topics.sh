#!/bin/bash

# Wait for Kafka to start
echo "Creating Kafka topics..."
kafka-topics --bootstrap-server localhost:9092 \
  --create --if-not-exists --topic NOTIFICATION_TOPIC \
  --replication-factor 1 --partitions 3

echo "Kafka topics created."