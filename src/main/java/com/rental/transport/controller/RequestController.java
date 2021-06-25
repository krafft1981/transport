package com.rental.transport.controller;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Request;
import com.rental.transport.service.RequestService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/request")
@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @ApiOperation(
            value = "Подтверждение в возможности выполнения заказа"
    )
    @PostMapping(value = "/confirm")
    public List<Event> doPostConfirmRequest(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        return requestService.confirmRequest(principal.getName(), requestId);
    }

    @ApiOperation(
            value = "Отказ в возможности выполнения заказа"
    )
    @PostMapping(value = "/reject")
    public List<Event> doPostRejectRequest(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        return requestService.rejectRequest(principal.getName(), requestId);
    }

    @ApiOperation(
            value = "Создание запроса на заказ"
    )
    @PostMapping
    public List<Event> doPostRequest(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = false) Integer[] hour) {

        return requestService.createRequest(principal.getName(), transport_id, day, hour);
    }

    @ApiOperation(
            value = "Получение группы запросов"
    )
    @GetMapping
    public List<Request> doGetRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long[] ids) {

        return requestService.getRequest(principal.getName(), ids);
    }

    @ApiOperation(
            value = "Получение списка запросов сделанных заказчиком"
    )
    @GetMapping(value = "/customer")
    public List<Event> doGetRequestAsCustomer(Principal principal) {
        return requestService.getRequestAsCustomer(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка запросов поступивших водителю"
    )
    @GetMapping(value = "/driver")
    public List<Event> doGetRequestAsDriver(Principal principal) {
        return requestService.getRequestAsDriver(principal.getName());
    }
}
