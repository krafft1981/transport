package com.rental.transport.controller;

import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import com.rental.transport.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/order")
@Controller
public class OrderController {

    @GetMapping
    public String goGetOrder(Model model) {
        Integer page = 0;
        Integer size = 100;

        try {

            List<Order> orderList = NetworkService
                    .getInstance()
                    .getOrderApi()
                    .doGetOrderList(page, size)
                    .execute()
                    .body();

            model.addAttribute("orders", orderList);
        }
        catch(IOException e) {

        }
        return "order";
    }

    @GetMapping(value = "/add")
    public String goGetAddOrder() {
        return "order-add";
    }
}
