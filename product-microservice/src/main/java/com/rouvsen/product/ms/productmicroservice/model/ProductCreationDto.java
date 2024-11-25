package com.rouvsen.product.ms.productmicroservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ProductCreationDto {
    String title;
    BigDecimal price;
    Integer quantity;
}
