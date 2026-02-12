package com.rental.transport.entity.template;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@Accessors(chain = true)
public class AbstractUpdatableEntity extends AbstractEntity {

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "timestamp with time zone default CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
