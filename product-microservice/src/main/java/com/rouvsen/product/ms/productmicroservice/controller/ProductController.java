package com.rouvsen.product.ms.productmicroservice.controller;

import com.rouvsen.product.ms.productmicroservice.model.KafkaException;
import com.rouvsen.product.ms.productmicroservice.model.ProductCreationDto;
import com.rouvsen.product.ms.productmicroservice.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    //Note; these codes are not recommended, we're doing for only testing some things regarding Kafka
    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductCreationDto creationDto) {
        String customerId = null;
        try {
            customerId = productService.createProductSyncVersion2(creationDto);
        } catch (Exception e) {
            log.error("Error message: {}, error: {}", e.getMessage(), e.toString());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new KafkaException(new Date(), e.getMessage(), "Path: v1/products"));
        }
        return ResponseEntity.status(CREATED).body(customerId);
    }

}
