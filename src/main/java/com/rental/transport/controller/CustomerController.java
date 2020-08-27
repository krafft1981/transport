package com.rental.transport.controller;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.dto.Customer;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.service.CustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/customer")
@RestController
public class CustomerController extends
        AbstractController<CustomerEntity, Customer, CustomerRepository, CustomerMapper, CustomerService> {

    public CustomerController(CustomerService service) {
        super(service);
    }
}
