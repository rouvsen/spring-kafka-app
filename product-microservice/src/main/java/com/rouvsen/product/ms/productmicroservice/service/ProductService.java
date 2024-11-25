package com.rouvsen.product.ms.productmicroservice.service;

import com.rouvsen.product.ms.productmicroservice.model.ProductCreatedEvent;
import com.rouvsen.product.ms.productmicroservice.model.ProductCreationDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public String createProductAsyncVersion(ProductCreationDto creationDto) {
        log.info("Main Thread: {}", Thread.currentThread().getName());

        String productId = UUID.randomUUID().toString();

        CompletableFuture<SendResult<String, ProductCreatedEvent>> completableFuture =
                kafkaTemplate.send(
                        "product-created-events-topic",
                        productId,
                        toProductCreatedEvent(productId, creationDto)
                );

        completableFuture.whenComplete((result, exception) -> {
            log.info("Kafka Async Thread: {}", Thread.currentThread().getName());
            if (Objects.nonNull(exception)) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully, metadata: {}", result.getRecordMetadata());
            }
        });

        log.info("Request and Response are completed!");
        return productId;
    }

    public String createProductSyncVersion1(ProductCreationDto creationDto) {
        log.info("Main Thread: {}", Thread.currentThread().getName());

        String productId = UUID.randomUUID().toString();

        CompletableFuture<SendResult<String, ProductCreatedEvent>> completableFuture =
                kafkaTemplate.send(
                        "product-created-events-topic",
                        productId,
                        toProductCreatedEvent(productId, creationDto)
                );

        completableFuture.whenComplete((result, exception) -> {
            log.info("Kafka Async Thread: {}", Thread.currentThread().getName());
            if (Objects.nonNull(exception)) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully, metadata: {}", result.getRecordMetadata());
            }
        });

        completableFuture.join(); //That'll join to the Main Thread, and will wait for acknowledge, * Guaranteed!

        log.info("Request and Response are completed!");
        return productId;
    }

    public String createProductSyncVersion2(ProductCreationDto creationDto) throws Exception {
        log.info("Main Thread: {}", Thread.currentThread().getName());

        String productId = UUID.randomUUID().toString();

        // Remove CompletableFuture, just keep SendResult that is sync version and will wait for acknowledge, * Guaranteed!
        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(
                        "product-created-events-topic",
                        productId,
                        toProductCreatedEvent(productId, creationDto)
                ).get();

        log.info("Request and Response are completed!");
        return productId;
    }

    private ProductCreatedEvent toProductCreatedEvent(String productId, ProductCreationDto creationDto) {
        return ProductCreatedEvent.builder()
                .productId(productId)
                .title(creationDto.getTitle())
                .price(creationDto.getPrice())
                .quantity(creationDto.getQuantity())
                .build();
    }

}
