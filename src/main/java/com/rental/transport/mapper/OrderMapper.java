package com.rental.transport.mapper;

import com.rental.transport.dto.OrderDto;
import com.rental.transport.entity.OrderEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                RequestMapper.class
        })
public interface OrderMapper {

    OrderEntity dtoToEntity(OrderDto dto);

    OrderDto entityToDto(OrderEntity entity);
}
