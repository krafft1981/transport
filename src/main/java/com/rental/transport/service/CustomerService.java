package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private void test() {
        final var entity = new CustomerEntity()
                .setId(UUID.randomUUID());
    }
}
