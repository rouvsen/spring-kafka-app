package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.exception.NonRetryableException;
import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.exception.RetryableException;
import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.model.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @SneakyThrows
    @KafkaHandler
    public void handle(String event) {
        ProductCreatedEvent productCreatedEvent = objectMapper.readValue(event, ProductCreatedEvent.class);
        log.info("Received a new event: {}", productCreatedEvent);
//        throw non retryable exception or retryable exception for visualize behaviour of consumer / event handler

        String requestUrl = "http://localhost:8082";

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Received response from a remote service: " + response.getBody());
            }
        } catch (ResourceAccessException exception) { // remote service (server) is not available
            log.info(exception.getMessage());
            throw new RetryableException(exception);
        } catch (HttpServerErrorException exception) { // 500 internal server error
            log.info(exception.getMessage());
            throw new NonRetryableException(exception);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new NonRetryableException(exception);
        }
    }

}
