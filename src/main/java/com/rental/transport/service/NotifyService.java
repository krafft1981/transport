package com.rental.transport.service;

import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.RequestEntity;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    public void createOrderMessage(OrderEntity order) {

        System.out.println("Post message: " + order.toString());
    }

    public void createRequest(RequestEntity request) {

        System.out.println("create request: " + request.toString());
    }

    public void confirmRequest(RequestEntity request) {

        System.out.println("confirm request: " + request.toString());
    }

    public void rejectRequest(RequestEntity request) {

        System.out.println("reject request: " + request.toString());
    }
}
