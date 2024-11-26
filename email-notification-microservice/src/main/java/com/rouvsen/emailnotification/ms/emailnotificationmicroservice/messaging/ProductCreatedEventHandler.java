package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.messaging;

import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.model.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void handle(ProductCreatedEvent event) {
        log.info("Received a new event: {}", event.getTitle());
    }

}
