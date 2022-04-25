package com.rental.transport.controller;

import com.rental.transport.service.ImageService;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/image")
@RestController
@AllArgsConstructor
public class ImageController {

    private final ImageService service;

    @ApiOperation(
        value = "Загрузка картинки"
    )
    @PostMapping
    public Long doPostImage(@RequestBody byte[] data) {

        return service.create(data);
    }

    @ApiOperation(
        value = "Получение картинки по id"
    )
    @GetMapping
    public byte[] doGetImage(@RequestParam(value = "id", required = true) Long id) throws ObjectNotFoundException {

        return service.getImage(id);
    }

    @ApiOperation(
        value = "Получение картинки по пути"
    )
    @GetMapping(
            value = "/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public String doGetImageByPath(@PathVariable Long id) throws ObjectNotFoundException {

        return service.getStringImage(id);
    }

    @ApiOperation(
        value = "Удаление картинки"
    )
    @DeleteMapping
    public void doDeleteImage(@RequestParam(value = "id", required = true) Long[] ids) {

        service.delete(Arrays.asList(ids));
    }

    @ApiOperation(
        value = "Получение списка доступных идентификаторов картинок"
    )
    @GetMapping(value = "/list")
    public List<Long> goGetPagesImage(
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}
