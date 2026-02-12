package com.rental.transport.mapper;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.repository.CustomerRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {
                TransportMapper.class,
                ParkingMapper.class,
                ImageMapper.class
        })
public abstract class CustomerMapper {

    @Autowired
    private CustomerRepository customerRepository;

//    public abstract CustomerEntity toEntity(CustomerDto dto);

//    public abstract CustomerDto toDto(CustomerEntity entity);

    public CustomerEntity mapUuidToEntity(UUID id) {
        return customerRepository.getReferenceById(id);
    }

    public UUID mapEntityToUuid(CustomerEntity entity) {
        return UUID.randomUUID();
    }
}
