package com.rental.transport.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/driver")
@RestController
public class DriverController {

    @GetMapping
    public void doGetDriverRequest (
            @RequestParam(value = "id", required = false) Long id) {
    }

    @PutMapping
    public void doPutDriverRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }

    @PostMapping
    public void doPostDriverRequest() {
    }

    @DeleteMapping
    public void doDeleteDriverRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }
}
