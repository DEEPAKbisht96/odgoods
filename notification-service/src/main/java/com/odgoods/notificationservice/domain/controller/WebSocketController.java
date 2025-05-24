package com.odgoods.notificationservice.domain.controller;


import com.odgoods.notificationservice.domain.dto.kafka_message.PushNotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(PushNotificationMessage message) {
        String destination = "/topic/notifications/" + message.getUserId();
        messagingTemplate.convertAndSend(destination, message);
    }
}
