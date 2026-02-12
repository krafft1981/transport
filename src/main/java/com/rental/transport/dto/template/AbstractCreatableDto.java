package com.rental.transport.dto.template;

import jakarta.persistence.MappedSuperclass;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@MappedSuperclass
@Accessors(chain = true)
public class AbstractCreatableDto extends AbstractDto {

    private LocalDateTime createdAt;
}
