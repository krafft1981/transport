package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractUpdatableDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PropertyDto extends AbstractUpdatableDto {

    private String name;
    private String value;
}
