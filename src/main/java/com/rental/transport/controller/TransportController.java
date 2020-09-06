package com.rental.transport.controller;

import com.rental.transport.dto.Parking;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.service.ParkingService;
import com.rental.transport.service.TransportService;
import java.security.Principal;
import java.util.List;
import lombok.NonNull;
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

@RequestMapping(value="/transport")
@RestController
public class TransportController {

    @Autowired
    private TransportService service;

    @DeleteMapping
    public void doDeleteTransportRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        service.delete(principal.getName(), id);
    }

    @GetMapping(value = "/list")
    public List<Transport> goGetPagesTransportRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @PostMapping
    public Long goPostTransportRequest(
            @NonNull Principal principal) {

        return service.create(principal.getName());
    }

    @PutMapping
    public void goPutTransportRequest(
            @NonNull Principal principal,
            @RequestBody Transport transport) {

        service.update(principal.getName(), transport);
    }
}

