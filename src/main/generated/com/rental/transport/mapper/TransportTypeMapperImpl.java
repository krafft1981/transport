package com.rental.transport.mapper;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.entity.TransportTypeEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class TransportTypeMapperImpl implements TransportTypeMapper {

    @Override
    public TransportTypeEntity toEntity(TransportTypeDto dto) {
        if ( dto == null ) {
            return null;
        }

        TransportTypeEntity transportTypeEntity = new TransportTypeEntity();

        transportTypeEntity.setId( dto.getId() );
        transportTypeEntity.setCreatedAt( dto.getCreatedAt() );
        transportTypeEntity.setEnable( dto.getEnable() );
        transportTypeEntity.setName( dto.getName() );

        return transportTypeEntity;
    }

    @Override
    public TransportTypeDto toDto(TransportTypeEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TransportTypeDto transportTypeDto = new TransportTypeDto();

        transportTypeDto.setId( entity.getId() );
        transportTypeDto.setCreatedAt( entity.getCreatedAt() );
        transportTypeDto.setEnable( entity.getEnable() );
        transportTypeDto.setName( entity.getName() );

        return transportTypeDto;
    }
}
