package com.rental.transport.service;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dto.Customer;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements AbstractMapper<CustomerEntity, Customer>{

    @Autowired
    private ModelMapper mapper;

    public CustomerEntity toEntity(Customer dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, CustomerEntity.class);
    }

    public Customer toDto(CustomerEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Customer.class);
    }

    @Override
    public CustomerEntity create() {
        CustomerEntity entity = new CustomerEntity();
        return entity;
    }
}
