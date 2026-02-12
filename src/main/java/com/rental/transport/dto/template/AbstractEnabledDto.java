package com.rental.transport.dto.template;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@MappedSuperclass
@Accessors(chain = true)
@Setter
@Getter
public class AbstractEnabledDto extends AbstractDto {

    private Boolean enable;
}
