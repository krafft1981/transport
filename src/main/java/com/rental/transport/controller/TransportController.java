package com.rental.transport.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/transport")
@RestController
public class TransportController {

    @GetMapping
    public void doGetTransportRequest (
            @RequestParam(value = "id", required = false) Long id) {
    }

    @PutMapping
    public void doPutTransportRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }

    @PostMapping
    public void doPostTransportRequest() {
    }

    @DeleteMapping
    public void doDeleteTransportRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }
}
