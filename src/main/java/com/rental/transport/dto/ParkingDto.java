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

    private Set<UUID> customer;
    private Set<UUID> transport;
    private Set<UUID> image;
    private Set<UUID> properties;
}
