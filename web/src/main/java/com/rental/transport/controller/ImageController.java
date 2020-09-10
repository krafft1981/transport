package com.rental.transport.controller;

import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value="/image")
@Controller
public class ImageController {

    @GetMapping
    public String goGetImage(Model model) {
        Integer page = 0;
        Integer size = 500;

        try {
            List<Long> imageList = NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImageList(page, size)
                    .execute()
                    .body();

            model.addAttribute("images", imageList);
        }
        catch(IOException e) {

        }

        return "image";
    }

    @GetMapping(value = { "/delete" })
    public String goDeleteImage(
            @RequestParam(value = "id", required = true) Long id,
            Model model) {

        Integer page = 0;
        Integer size = 500;

        Long[] ids = new Long[1];
        ids[0] = id;

        try {
            NetworkService
                .getInstance()
                .getImageApi()
                .doDeleteImage(ids)
                .execute();

            List<Long> imageList = NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImageList(page, size)
                    .execute()
                    .body();

            model.addAttribute("images", imageList);
        }
        catch(IOException e) {

        }

        return "image";
    }

    @GetMapping(value = "/add")
    public String goGetAddImage() {
        return "image-add";
    }
}
