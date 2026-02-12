package com.rental.transport.mapper;

import com.rental.transport.dto.RequestDto;
import com.rental.transport.entity.RequestEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                TransportMapper.class,
                CustomerMapper.class,
                PropertyMapper.class
        })
public interface RequestMapper {

    RequestEntity dtoToEntity(RequestDto dto);

    RequestDto entityToDto(RequestEntity entity);
}
