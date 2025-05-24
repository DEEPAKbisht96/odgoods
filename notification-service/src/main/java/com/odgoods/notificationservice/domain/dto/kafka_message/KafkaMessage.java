package com.odgoods.notificationservice.domain.dto.kafka_message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {
    private String message;
    private String topic;
    private String type;
    private Long userId;
}

