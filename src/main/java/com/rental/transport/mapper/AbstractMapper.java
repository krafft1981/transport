package com.rental.transport.mapper;

import com.rental.transport.dto.AbstractDto;
import com.rental.transport.entity.AbstractEntity;
import org.modelmapper.Converter;

public interface AbstractMapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);
    D toDto(E entity);
    E create();

    default Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    default Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    default void mapSpecificFields(E source, D destination) {

    }

    default void mapSpecificFields(D source, E destination) {

    }
}
