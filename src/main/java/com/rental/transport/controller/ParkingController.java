package com.rental.transport.controller;

import com.rental.transport.dto.Parking;
import com.rental.transport.service.ParkingService;
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

@RequestMapping(value = "/parking")
@RestController
public class ParkingController {

    @Autowired
    private ParkingService service;

    @DeleteMapping
    public void doDeleteParkingRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        service.delete(principal.getName(), id);
    }

    @GetMapping(value = "/list")
    public List<Parking> goGetPagesParkingRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @PostMapping
    public Long goPostParkingRequest(
            Principal principal) {

        return service.create(principal.getName());
    }

    @GetMapping(value = "/my")
    public List<Parking> doGetMyParkingRequest(Principal principal) {

        return service.getMyParking(principal.getName());
    }

    @PutMapping
    public void goPutParkingRequest(
            Principal principal,
            @RequestBody Parking parking) {

        service.update(principal.getName(), parking);
    }
}
