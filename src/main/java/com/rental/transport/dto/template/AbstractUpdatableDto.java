package com.rental.transport.dto.template;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@Accessors(chain = true)
public class AbstractUpdatableDto extends AbstractDto {

    private LocalDateTime updatedAt;
}
