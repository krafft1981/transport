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
    private Set<UUID> images;
    private Set<UUID> transports;
    private Set<UUID> parkings;
    private Set<UUID> propertyes;
}
