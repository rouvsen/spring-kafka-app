package com.rouvsen.product.ms.productmicroservice.controller;

import com.rouvsen.product.ms.productmicroservice.model.ProductCreationDto;
import com.rouvsen.product.ms.productmicroservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductCreationDto creationDto) {
        return ResponseEntity.status(CREATED).body(productService.createProductAsyncVersion(creationDto));
    }

}
