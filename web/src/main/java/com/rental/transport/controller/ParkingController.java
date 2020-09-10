package com.rental.transport.controller;

import com.rental.transport.model.Parking;
import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value="/parking")
@Controller
public class ParkingController {

    @GetMapping
    public String goGetParking(Model model) {
        Integer page = 0;
        Integer size = 100;

        try {

            List<Parking> parkingList = NetworkService
                    .getInstance()
                    .getParkingApi()
                    .doGetParkingList(page, size)
                    .execute()
                    .body();

            model.addAttribute("parkings", parkingList);
        }
        catch(IOException e) {

        }

        return "parking";
    }

    @GetMapping(value = { "/delete" })
    public String goDeleteParking(
            @RequestParam(value = "id", required = true) Long id,
            Model model) {

        Integer page = 0;
        Integer size = 100;

        try {
            NetworkService
                    .getInstance()
                    .getParkingApi()
                    .doDeleteParking(id)
                    .execute();

            List<Parking> parkingList = NetworkService
                    .getInstance()
                    .getParkingApi()
                    .doGetParkingList(page, size)
                    .execute()
                    .body();

            model.addAttribute("parkings", parkingList);
        }
        catch(IOException e) {

        }

        return "parking";
    }

    @GetMapping(value = "/add")
    public String goGetAddParking() {
        return "parking-add";
    }
}
