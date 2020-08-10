package com.rental.transport.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/parking")
@RestController
public class ParkingController {

    @GetMapping
    public void doGetParkingRequest (
            @RequestParam(value = "id", required = false) Long id) {
    }

    @PutMapping
    public void doPutParkingRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }

    @PostMapping
    public void doPostParkingRequest() {
    }

    @DeleteMapping
    public void doDeleteParkingRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }
}
