package com.odgoods.orderservice.domain.dto.kafka_message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PushNotificationMessage extends KafkaMessage {

    private String title;

    public PushNotificationMessage(String topic, String message, String title, Long userId) {
        super(message, topic, "PUSH_NOTIFICATION", userId);
        this.title = title;
    }
}

