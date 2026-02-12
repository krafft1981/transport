package com.rental.transport.mapper;

import com.rental.transport.dto.CustomerDto;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.repository.CustomerRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                ParkingMapper.class,
                TransportMapper.class,
                PropertyMapper.class,
                ImageMapper.class
        })
public abstract class CustomerMapper {

    @Autowired
    private CustomerRepository customerRepository;

    public abstract CustomerEntity dtoToEntity(CustomerDto dto);
    public abstract CustomerDto entityToDto(CustomerEntity entity);

    public CustomerEntity uuidToEntity(UUID id) {
        return customerRepository.getReferenceById(id);
    }

    public UUID entityToUuid(CustomerEntity entity) {
        return entity.getId();
    }
}
