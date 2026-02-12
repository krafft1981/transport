package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DistDto extends AbstractEnabledDto {

    private String program;
}
