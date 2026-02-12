package com.rental.transport.entity.template;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@Accessors(chain = true)
public class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp with time zone default CURRENT_TIMESTAMP")
    LocalDateTime createdAt;
}
