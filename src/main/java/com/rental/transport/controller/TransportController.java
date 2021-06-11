package com.rental.transport.controller;

import com.rental.transport.dto.Transport;
import com.rental.transport.service.TransportService;
import io.swagger.annotations.ApiOperation;
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

@RequestMapping(value = "/transport")
@RestController
public class TransportController {

    @Autowired
    private TransportService service;

    @ApiOperation(
            value = "Удаление транспортного средства"
    )
    @DeleteMapping
    public void doDeleteTransport(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        service.delete(principal.getName(), id);
    }

    @ApiOperation(
            value = "Получение списка доступных транспортных средств"
    )
    @GetMapping(value = "/list")
    public List<Transport> doGetPagesTransport(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @ApiOperation(
            value = "Получение списка доступных транспортных средств заданного типа"
    )
    @GetMapping(value = "/list/type")
    public List<Transport> doGetPagesTransportByType(
            @RequestParam(value = "type", required = true) Long type,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPageTyped(pageable, type);
    }

    @ApiOperation(
            value = "Создание транспортного средства"
    )
    @PostMapping
    public Long doPostTransport(
            Principal principal,
            @RequestParam(value = "type", required = true) String type) {

        return service.create(principal.getName(), type);
    }

    @ApiOperation(
            value = "Редактирование транспортного средства"
    )
    @PutMapping
    public void doPutTransport(
            Principal principal,
            @RequestBody Transport transport) {

        service.update(principal.getName(), transport);
    }

    @ApiOperation(
            value = "Получение списка своих транспортных средств"
    )
    @GetMapping(value = "/my")
    public List<Transport> doGetMyTransport(Principal principal) {

        return service.getMyTransport(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка транспортных средств закреплённых за стоянкой"
    )
    @GetMapping(value = "/parking")
    public List<Transport> doGetParkingTransport(
            @RequestParam(value = "parking_id", required = true) Long parkingId) {

        return service.getParkingTransport(parkingId);
    }

    @ApiOperation(
            value = "Добавление картинки к транспорту"
    )
    @PostMapping(value = "/image")
    public Transport doPostTransportImage(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transportId,
            @RequestBody byte[] data) {

        return service.addTransportImage(principal.getName(), transportId, data);
    }

    @ApiOperation(
            value = "Удаление картинки из транспорта"
    )
    @DeleteMapping(value = "/image")
    public Transport doDeleteTransportImage(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transportId,
            @RequestParam(value = "image_id", required = true) Long imageId) {

        return service.delTransportImage(principal.getName(), transportId, imageId);
    }
}
