package com.rental.transport.controller;

import com.rental.transport.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService service;

//    @ApiOperation(
//            value = "Загрузка картинки"
//    )
    @PostMapping
    public UUID doPostImage(@RequestBody byte[] data) {

        return service.create(data);
    }

//    @ApiOperation(
//            value = "Получение картинки по id"
//    )
    @GetMapping
    public byte[] doGetImage(@RequestParam(value = "id", required = true) UUID id) {

        return service.getImage(id);
    }

//    @ApiOperation(
//            value = "Получение картинки по пути"
//    )
    @GetMapping(
            value = "/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public String doGetImageByPath(@PathVariable UUID id) {

        return service.getStringImage(id);
    }

//    @ApiOperation(
//            value = "Удаление картинки"
//    )
    @DeleteMapping
    public void doDeleteImage(@RequestParam(value = "id", required = true) UUID[] ids) {

        service.delete(Arrays.asList(ids));
    }

//    @ApiOperation(
//            value = "Получение списка доступных идентификаторов картинок"
//    )
    @GetMapping(value = "/list")
    public List<UUID> goGetPagesImage(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}
