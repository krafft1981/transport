package com.rental.transport.controller;

import com.rental.transport.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/registration")
@RestController
public class RegistrationController {

    @Autowired
    private CustomerService service;

    @PostMapping
    public Long doPostRegistrationRequest(
            @RequestParam(value = "account", required = true) String account) {

        return service.create(account);
    }

    @GetMapping(value = "/exist")
    public Boolean doGetExistCustomerRequest(
            @RequestParam(value = "account", required = true) String account) {

        return service.exist(account);
    }
}
