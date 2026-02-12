package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ParkingDto extends AbstractEnabledDto {

    private Set<UUID> customers;
    private Set<UUID> transports;
    private Set<UUID> images;
    private Set<UUID> propertyes;
}
