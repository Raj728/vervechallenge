package com.technicalchallenge.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerUtil {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    public KafkaProducerUtil(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUniqueCount(int count) {
        String message = "{ \"uniqueCount\": " + count + " }";
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message to Kafka: " + message);
    }
}

