package com.odgoods.orderservice.kafka;


import com.odgoods.orderservice.domain.model.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrderKafkaProducer {

    private final KafkaTemplate<KafkaTopics, Object> kafkaTemplate;

    public OrderKafkaProducer(KafkaTemplate<KafkaTopics, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Object message) {
        try {
            kafkaTemplate.send(topic, message);
            System.out.println("Message sent to topic: " + topic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
