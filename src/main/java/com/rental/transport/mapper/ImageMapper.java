package com.rental.transport.mapper;

import com.rental.transport.dto.Image;
import com.rental.transport.entity.ImageEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper implements AbstractMapper<ImageEntity, Image> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public ImageEntity toEntity(Image dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, ImageEntity.class);
    }

    @Override
    public Image toDto(ImageEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Image.class);
    }
}
