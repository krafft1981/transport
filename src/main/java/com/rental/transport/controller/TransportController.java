package com.rental.transport.controller;

import com.rental.transport.dto.Transport;
import com.rental.transport.service.TransportService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TransportService transportService;

    @GetMapping
    public Transport doGetTransportRequest(
            @RequestParam(value = "id", required = true) Long id) {
        return transportService.findById(id);
    }

    @GetMapping(value = "/list")
    public List<Transport> doGetTransportListRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        return transportService.getList(page, size);
    }

    @PostMapping
    public Long doPostTransportRequest(
            Principal principal,
            @RequestParam(value = "type", required = true) String type) {
        return transportService.newTransport(principal.getName(), type);
    }

    @DeleteMapping
    public void doDeleteTransportRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {
        transportService.deleteTransport(principal.getName(), id);
    }

    @PutMapping
    public void doPutTransportRequest(
            Principal principal,
            @RequestParam(value = "transport", required = true) Transport transport) {
        transportService.updateTransport(principal.getName(), transport);
    }
}
