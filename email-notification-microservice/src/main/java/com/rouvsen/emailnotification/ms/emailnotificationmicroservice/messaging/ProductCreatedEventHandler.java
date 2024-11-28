package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.model.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaHandler
    public void handle(String event) {
        ProductCreatedEvent productCreatedEvent = objectMapper.readValue(event, ProductCreatedEvent.class);
        log.info("Received a new event: {}", productCreatedEvent);
    }

}
