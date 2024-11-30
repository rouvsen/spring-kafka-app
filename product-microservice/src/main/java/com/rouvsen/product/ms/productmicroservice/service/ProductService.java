package com.rouvsen.product.ms.productmicroservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouvsen.product.ms.productmicroservice.model.ProductCreatedEvent;
import com.rouvsen.product.ms.productmicroservice.model.ProductCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public String createProductAsyncVersion(ProductCreationDto creationDto) {
        log.info("Main Thread: {}", Thread.currentThread().getName());

        String productId = UUID.randomUUID().toString();

        CompletableFuture<SendResult<String, String>> completableFuture =
                kafkaTemplate.send(
                        "product-created-events-topic",
                        productId,
                        toProductCreatedEventJson(productId, creationDto)
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

        CompletableFuture<SendResult<String, String>> completableFuture =
                kafkaTemplate.send(
                        "product-created-events-topic",
                        productId,
                        toProductCreatedEventJson(productId, creationDto)
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

        log.info("Message will be sent to Kafka Topic!");

        // Remove CompletableFuture, just keep SendResult that is sync version and will wait for acknowledge, * Guaranteed!
//        SendResult<String, String> result =
//                kafkaTemplate.send(
//                        "product-created-events-topic",
//                        productId,
//                        toProductCreatedEventJson(productId, creationDto)
//                ).get();

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                toProductCreatedEventJson(productId, creationDto)
        );
        producerRecord.headers().add("messageId", UUID.randomUUID().toString().getBytes(UTF_8));

        SendResult<String, String> result = kafkaTemplate.send(producerRecord).get();

        log.info("""
                        Record metadata:
                        Timestamp: [ {} ],
                        Topic: [ {} ],
                        Partition: [ {} ],
                        Offset: [ {} ]
                        """,
                Instant.ofEpochMilli(result.getRecordMetadata().timestamp()),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());

        log.info("Request and Response are completed!");
        return productId;
    }

    @SneakyThrows
    private String toProductCreatedEventJson(String productId, ProductCreationDto creationDto) {
        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(productId)
                .title(creationDto.getTitle())
                .price(creationDto.getPrice())
                .quantity(creationDto.getQuantity())
                .build();
        return objectMapper.writeValueAsString(event);
    }

}
