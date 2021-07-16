package com.rental.transport.controller;

import com.rental.transport.dto.Customer;
import com.rental.transport.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/registration")
@RestController
public class RegistrationController {

    @Autowired
    private CustomerService service;

    @ApiOperation(
        value = "Регистрирование пользователя"
    )
    @PostMapping
    public Customer doPostRegistration(
        @RequestParam(value = "account", required = true) String account,
        @RequestParam(value = "password", required = true) String password,
        @RequestParam(value = "phone", required = true) String phone,
        @RequestParam(value = "fio", required = true) String fio,
        @RequestParam(value = "time_zone", required = true) String tz) {

        return service.create(account, password, phone, fio, tz);
    }

    @ApiOperation(
        value = "Подтверждение регистрации пользователя"
    )
    @PutMapping
    public void doPostRegistrationConfirm(
        @RequestParam(value = "account", required = true) String account) {

        service.confirm(account);
    }

    @ApiOperation(
        value = "Получение реквизитов пользователя на почту"
    )
    @PostMapping(value = "/email")
    public void doPostRegistrationEmailCheck(
        @RequestParam(value = "account", required = true) String account) {

        service.check(account);
    }
}
