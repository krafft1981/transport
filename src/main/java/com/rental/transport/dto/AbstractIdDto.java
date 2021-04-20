package com.rental.transport.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractIdDto extends AbstractDto {

    private Long id;
}
