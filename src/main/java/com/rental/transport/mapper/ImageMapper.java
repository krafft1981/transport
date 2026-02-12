package com.rental.transport.mapper;

import com.rental.transport.entity.ImageEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    public abstract ImageEntity toEntity(UUID id);
    public UUID toDto(ImageEntity entity) {
        return UUID.randomUUID();
    }
}
