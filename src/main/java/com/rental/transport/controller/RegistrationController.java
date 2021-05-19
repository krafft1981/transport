package com.rental.transport.controller;

import com.rental.transport.dto.Customer;
import com.rental.transport.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/registration")
@RestController
public class RegistrationController {

    @Autowired
    private CustomerService service;

    @PostMapping
    public Customer doPostRegistration(
            @RequestParam(value = "account", required = true) String account,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "fio", required = true) String fio) {

        return service.create(account, password, phone, fio);
    }

    @PutMapping
    public void doPostRegistrationConfirm(
            @RequestParam(value = "account", required = true) String account) {

        service.confirm(account);
    }

    @PostMapping(value = "/email")
    public void doPostRegistrationEmailCheck(
            @RequestParam(value = "account", required = true) String account) {

        service.check(account);
    }
}
