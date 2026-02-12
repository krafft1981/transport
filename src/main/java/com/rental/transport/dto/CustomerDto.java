package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class CustomerDto extends AbstractEnabledDto {

    private String timeZone;
    private Set<UUID> image;
    private Set<UUID> transport;
    private Set<UUID> parking;
    private Set<UUID> properties;
}
