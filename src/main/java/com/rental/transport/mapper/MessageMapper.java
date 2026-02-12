package com.rental.transport.mapper;

import com.rental.transport.dto.MessageDto;
import com.rental.transport.entity.MessageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageEntity toEntity(MessageDto dto);
    MessageDto toDto(MessageEntity entity);
}
