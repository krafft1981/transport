package com.rental.transport.mapper;

import com.rental.transport.dto.MessageDto;
import com.rental.transport.entity.MessageEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.HashSet;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                CustomerMapper.class
        })
public interface MessageMapper {

    MessageEntity dtoToEntity(MessageDto dto);
    MessageDto entityToDto(MessageEntity entity);
}
