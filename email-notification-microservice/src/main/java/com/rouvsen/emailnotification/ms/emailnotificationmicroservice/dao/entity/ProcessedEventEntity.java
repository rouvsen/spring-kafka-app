package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Table(name = "processed-event")
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEventEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String messageId;

    @Column(nullable = false)
    String productId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProcessedEventEntity that = (ProcessedEventEntity) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messageId);
    }
}
