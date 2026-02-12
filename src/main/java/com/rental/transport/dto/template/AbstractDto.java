package com.rental.transport.dto.template;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@Accessors(chain = true)
public class AbstractDto {

    private UUID id;
    LocalDateTime createdAt;
}
