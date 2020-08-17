package com.rental.transport.controller;

import com.rental.transport.dto.Parking;
import com.rental.transport.service.ParkingService;
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

@RequestMapping(value="/parking")
@RestController
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public Parking doGetParkingRequest (
            @RequestParam(value = "id", required = true) Long id) {
        return parkingService.findById(id);
    }

    @GetMapping(value = "/list")
    public List<Parking> doGetParkingListRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        return parkingService.getList(page, size);
    }

    @PutMapping
    public void doPutParkingRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {
    }

    @PostMapping
    public Long doPostParkingRequest(Principal principal) {
        return parkingService.newParking(principal.getName());
    }

    @DeleteMapping
    public void doDeleteParkingRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {
    }
}
