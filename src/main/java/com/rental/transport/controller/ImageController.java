package com.rental.transport.controller;

import com.rental.transport.service.ImageService;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService service;

    @PostMapping
    public Long doPostImage(@RequestBody byte[] data) {

        return service.create(data);
    }

    @GetMapping
    public byte[] doGetImage(@RequestParam(value = "id", required = true) Long id) throws ObjectNotFoundException {

        return service.getImage(id);
    }

    @DeleteMapping
    public void doDeleteImage(@RequestParam(value = "id", required = true) Long[] ids) {

        service.delete(Arrays.asList(ids));
    }

    @GetMapping(value = "/list")
    public List<Long> goGetPagesImage(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}
