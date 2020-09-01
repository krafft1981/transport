package com.rental.transport.dto;

import java.sql.Blob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Type extends AbstractDto {

    private String name;
    private Blob image;
}
