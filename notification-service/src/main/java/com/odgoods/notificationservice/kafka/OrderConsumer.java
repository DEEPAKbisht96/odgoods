package com.odgoods.notificationservice.kafka;


import com.odgoods.notificationservice.domain.controller.WebSocketController;
import com.odgoods.notificationservice.domain.dto.kafka_message.PushNotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final WebSocketController webSocketController;

    @KafkaListener(topics = "NOTIFICATION_TOPIC", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenOrderTopic(PushNotificationMessage message) {

        System.out.println("Received title: " + message.getTitle());
        System.out.println("Received userid: " + message.getUserId());
        System.out.println("Received topic: " + message.getTopic());
        System.out.println("Received type: " + message.getType());
        System.out.println("Received message: " + message.getMessage());

        webSocketController.sendNotificationToUser(message);

    }
}
