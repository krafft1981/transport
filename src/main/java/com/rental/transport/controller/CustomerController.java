package com.rental.transport.controller;

import com.rental.transport.dto.Customer;
import com.rental.transport.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService service;

    @ApiOperation(
            value = "Редактирование пользователя"
    )
    @PutMapping
    public void doPutUpdateCustomer(
            Principal principal,
            @RequestBody Customer dto) {

        service.update(principal.getName(), dto);
    }

    @ApiOperation(
            value = "Редактирование пароля пользователя"
    )
    @PutMapping(value = "/update/password")
    public void doPutUpdateCustomerPassword(
            Principal principal,
            @RequestParam(value = "password", required = true) String password) {

        service.updatePassword(principal.getName(), password);
    }

    @ApiOperation(
            value = "Получение списка доступных пользователей"
    )
    @GetMapping(value = "/list")
    public List<Customer> doGetPagesCustomer(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @ApiOperation(
            value = "Получение пользователя"
    )
    @GetMapping
    public Customer doGetCustomer(
            Principal principal) {

        return service.getDto(principal.getName());
    }

    @ApiOperation(
            value = "Добавление картинки к пользователю"
    )
    @PostMapping(value = "/image")
    public Customer doPostCustomerImage(
            Principal principal,
            @RequestBody byte[] data) {

        return service.addCustomerImage(principal.getName(), data);
    }

    @ApiOperation(
            value = "Удаление картинки из пользователю"
    )
    @DeleteMapping(value = "/image")
    public Customer doDeleteCustomerImage(
            Principal principal,
            @RequestParam(value = "image_id", required = true) Long imageId) {

        return service.delCustomerImage(principal.getName(), imageId);
    }
}
