package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDto extends AbstractDto {

    private RequestDto request;
}
