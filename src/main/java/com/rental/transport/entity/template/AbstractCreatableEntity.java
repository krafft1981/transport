package com.rental.transport.entity.template;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Accessors(chain = true)
public class AbstractCreatableEntity extends AbstractEntity {

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
