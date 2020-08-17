package com.rental.transport.controller;

import com.rental.transport.dto.Customer;
import com.rental.transport.service.CustomerService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Customer doGetCustomerRequest(
            Principal principal) {

        Customer customer = customerService.getCustomer(principal.getName());
        return customer;
    }

    @GetMapping(value = "/list")
    public List<Customer> doGetCustomerListRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        return customerService.getList(page, size);
    }

    @PutMapping
    public void doPutCustomerRequest(
            Principal principal,
            @RequestBody Customer customer) {
        customer.setAccount(principal.getName());
        customerService.setCustomer(customer);
    }
}
