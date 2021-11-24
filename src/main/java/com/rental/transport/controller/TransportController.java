package com.rental.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.dto.Transport;
import com.rental.transport.service.TransportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequestMapping(value = "/transport")
@RestController
public class TransportController {

    @Autowired
    private ObjectMapper objectMapper;

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
    public ResponseEntity<List<Transport>> doGetPagesTransport(
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<Transport> response = service.getPage(pageable);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Получение списка доступных транспортных средств заданного типа"
    )
    @GetMapping(value = "/list/type")
    public ResponseEntity<List<Transport>> doGetPagesTransportByType(
        @RequestParam(value = "type", required = true) Long type,
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<Transport> response = service.getPageTyped(pageable, type);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Создание транспортного средства"
    )
    @PostMapping
    public Long doPostTransport(
        Principal principal,
        @RequestParam(value = "type", required = true) Long typeId) {

        return service.create(principal.getName(), typeId);
    }

    @ApiOperation(
        value = "Редактирование транспортного средства"
    )
    @PutMapping
    public ResponseEntity<Transport> doPutTransport(
        Principal principal,
        @RequestBody Transport transport) throws Exception {
        Transport response = service.update(principal.getName(), transport);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Получение списка своих транспортных средств"
    )
    @GetMapping(value = "/my")
    public ResponseEntity<List<Transport>> doGetMyTransport(Principal principal) throws Exception {

        List<Transport> response = service.getMyTransport(principal.getName());
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Получение списка транспортных средств закреплённых за стоянкой"
    )
    @GetMapping(value = "/parking")
    public ResponseEntity<List<Transport>> doGetParkingTransport(
        @RequestParam(value = "parking_id", required = true) Long parkingId) throws Exception {
        List<Transport> response = service.getParkingTransport(parkingId);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Добавление картинки к транспорту"
    )
    @PostMapping(value = "/image")
    public ResponseEntity<Transport> doPostTransportImage(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId,
        @RequestBody byte[] data) throws Exception {
        Transport response = service.addTransportImage(principal.getName(), transportId, data);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Удаление картинки из транспорта"
    )
    @DeleteMapping(value = "/image")
    public ResponseEntity<Transport> doDeleteTransportImage(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId,
        @RequestParam(value = "image_id", required = true) Long imageId) throws Exception {
        Transport response = service.delTransportImage(principal.getName(), transportId, imageId);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }
}
