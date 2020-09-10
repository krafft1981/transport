
package com.rental.transport.controller;

import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value="/transport")
@Controller
public class TransportController {

    @GetMapping
    public String goGetTransport(Model model) {
        Integer page = 0;
        Integer size = 100;

        try {
            List<Transport> transportList = NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doGetTransportList(page, size)
                    .execute()
                    .body();

            model.addAttribute("transports", transportList);
        }
        catch(IOException e) {

        }
        return "transport";
    }

    @GetMapping(value = { "/delete" })
    public String goDeleteTransport(
            @RequestParam(value = "id", required = true) Long id,
            Model model) {

        Integer page = 0;
        Integer size = 100;

        try {
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doDeleteTransport(id)
                    .execute();

            System.out.println("Delete");

            List<Transport> transportList = NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doGetTransportList(page, size)
                    .execute()
                    .body();

            model.addAttribute("transports", transportList);
        }
        catch(IOException e) {

        }

        return "transport";
    }

    @GetMapping(value = "/add")
    public String goGetAddTransport() {
        return "transport-add";
    }
}
