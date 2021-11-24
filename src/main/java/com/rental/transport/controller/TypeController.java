package com.rental.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.dto.Type;
import com.rental.transport.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/type")
@RestController
public class TypeController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TypeService service;

    @ApiOperation(
        value = "Создание типа транспорта"
    )
    @PostMapping
    public Long doPostType(
        @RequestParam(value = "name", required = true) String name) {

        return service.create(name);
    }

    @ApiOperation(
        value = "Получение списка доступных типов транспорта"
    )
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Type>> doGetListType(
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<Type> response = service.getPage(pageable);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }
}
