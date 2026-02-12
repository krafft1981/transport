package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TransportDto extends AbstractEnabledDto {

    private TransportTypeDto type;
    private Set<UUID> images;
    private Set<UUID> customers;
    private Set<UUID> parkings;
    private Set<UUID> propertyes;
}
