package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
//@ToString(exclude = {"productId", "title", "price"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ProductCreatedEvent {
    String productId;
    String title;
    BigDecimal price;
    Integer quantity;
}
