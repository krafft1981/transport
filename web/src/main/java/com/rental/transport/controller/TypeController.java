package com.rental.transport.controller;

import com.rental.transport.model.Type;
import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/type")
@Controller
public class TypeController {

    @GetMapping
    public String goGetType(Model model) {

        Integer page = 0;
        Integer size = 100;

        try {

            List<Type> typeList = NetworkService
                    .getInstance()
                    .getTypeApi()
                    .doGetTypeList(page, size)
                    .execute()
                    .body();

            model.addAttribute("types", typeList);
        }
        catch(IOException e) {

        }
        return "type";
    }

    @GetMapping(value = "/add")
    public String goGetAddType() {
        return "type-add";
    }
}
