package com.rental.transport.controller;

import com.rental.transport.dto.Image;
import com.rental.transport.service.ImageService;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService service;

    @PostMapping
    public Long doPutImageRequest(@RequestBody String image) {

        return service.create(image);
    }

    @GetMapping
    public Image doGetImageRequest(@RequestParam(value =  "id", required = true) Long id) {

        return service.getImage(id);
    }

    @GetMapping(
            value="/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] doGetImageByPathRequest (
            @PathVariable(value =  "id") Long id) throws UnsupportedEncodingException {

        Image image = service.getImage(id);
        byte[] base64Bytes = image.getData().getBytes();
        byte[] decodedBytes = Base64.decode(base64Bytes);
        return decodedBytes;
    }

    @DeleteMapping
    public void doDeleteImageRequest(@RequestParam(value =  "id", required = true) Long[] ids) {

        service.delete(Arrays.asList(ids));
    }

    @GetMapping(value = "/list")
    public List<Long> goGetPagesImageRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @GetMapping(value = "/count")
    public Long doGetCountRequest() {

        return service.count();
    }
}
