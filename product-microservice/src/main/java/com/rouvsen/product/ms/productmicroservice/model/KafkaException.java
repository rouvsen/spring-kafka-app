package com.rouvsen.product.ms.productmicroservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class KafkaException {
    Date date;
    String message;
    String details;
}
