package com.rental.transport.controller;

import com.rental.transport.model.Customer;
import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/customer")
@Controller
public class CustomerController {
    @GetMapping
    public String goGetCustomer(Model model) {
        Integer page = 0;
        Integer size = 100;

        try {

            List<Customer> customerList = NetworkService
                    .getInstance()
                    .getCustomerApi()
                    .doGetCustomerList(page, size)
                    .execute()
                    .body();

            model.addAttribute("customers", customerList);
        }
        catch(IOException e) {

        }
        return "customer";
    }
}
