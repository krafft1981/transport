package com.rental.transport.controller;

import com.rental.transport.dto.Type;
import com.rental.transport.service.TypeService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/type")
@RestController
@AllArgsConstructor
public class TypeController {

    private final TypeService service;

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
    public List<Type> doGetListType(
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}
