package com.rental.transport.controller;

import com.rental.transport.dto.Property;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.service.PropertyService;
import com.rental.transport.service.TemplatesService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/property")
@RestController
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private TemplatesService templatesService;

    @GetMapping(value = "/value/count")
    public Long doGetValueCountRequest() {
        return propertyService.count();
    }

    @GetMapping(value = "/value/list")
    public List<Property> goGetPagesPropertyRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return propertyService.getPage(pageable);
    }

    @GetMapping(value = "/templates/count")
    public Long doGetTemplatesCountRequest() {
        return templatesService.count();
    }

    @GetMapping(value = "/templates")
    public void doGetTemplatesRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

//        templatesService.getDto(id);
    }

    @DeleteMapping
    public void doDeleteTemplatesRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

//        templatesService.delete(id);
    }

//    @PostMapping
//    public Long doPostTemplatesRequest(
//            Principal principal,
//            @RequestParam(value = "transport_type_id", required = true) String type_id,
//            @RequestParam(value = "value", required = true) String value,
//            @RequestParam(value = "type", required = true) String type) {
//
//        private String humanName = "";
//        private String logicName = "";
//        private String type = "String";
//
//        private PropertyTypeEntity name;
//
////        return service.create(principal.getName(), type);
//    }

    @PutMapping
    public void doPutTemplatesRequest(
            Principal principal,
            @RequestBody Transport transport) {

//        service.update(principal.getName(), transport);
    }
}
