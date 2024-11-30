package com.rouvsen.emailnotification.ms.emailnotificationmicroservice.dao.repository;

import com.rouvsen.emailnotification.ms.emailnotificationmicroservice.dao.entity.ProcessedEventEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedEventRepository extends CrudRepository<ProcessedEventEntity, Long> {
    Optional<ProcessedEventEntity> findByMessageId(String messageId);
}
