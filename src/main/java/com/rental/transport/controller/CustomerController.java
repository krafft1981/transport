package com.rental.transport.controller;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dao.CustomerRepository;
import com.rental.transport.dto.Customer;
import com.rental.transport.service.CustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/customer")
@RestController
public class CustomerController extends AbstractController<CustomerEntity, Customer, CustomerRepository, CustomerService> {

    public CustomerController(CustomerService service) {
        super(service);
    }
}
