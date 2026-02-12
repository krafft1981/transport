package com.rental.transport.controller;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/type")
@RestController
@RequiredArgsConstructor
public class TypeController {

    private final TypeService service;

//    @ApiOperation(
//        value = "Создание типа транспорта"
//    )
    @PostMapping
    public UUID doPostType(
        @RequestParam(value = "name", required = true) String name) {
        return service.create(name);
    }

//    @ApiOperation(
//        value = "Получение списка доступных типов транспорта"
//    )
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TransportTypeDto> doGetListType(
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}
