package com.rental.transport.controller;

import com.rental.transport.TransportApplication;
import com.rental.transport.dto.UserProfile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/customer")
@RestController
public class CustomerController {

    @GetMapping
    public UserProfile doGetCustomerRequest(
            @RequestParam(value = "id", required = false) Long id) {

        UserProfile user = new UserProfile();
        System.out.println(user.toString());
        return user;
    }

    @PutMapping
    public UserProfile doPutCustomerRequest(
            @RequestHeader(TransportApplication.USER_HEADER) UserProfile user,
            @RequestParam(value = "id", required = false) Long id) {

        return new UserProfile();
    }

    @PostMapping
    public void doPostCustomerRequest() {
    }

    @DeleteMapping
    public void doDeleteCustomerRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }
}
