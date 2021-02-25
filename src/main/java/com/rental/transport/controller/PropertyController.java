package com.rental.transport.controller;

import com.rental.transport.dto.Property;
import com.rental.transport.dto.PropertyType;
import com.rental.transport.dto.Templates;
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
    private TemplatesService templatesService;

    @Autowired
    private PropertyService propertyService;

    @GetMapping(value = "/count")
    public Long doGetValueCountRequest() {
        return propertyService.count();
    }

    @GetMapping(value = "/list")
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

    @GetMapping(value = "/templates/list")
    public List<Templates> goGetPagesTemplatesRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return templatesService.getTemplatesPage(pageable);
    }

    @GetMapping(value = "/templates")
    public Templates doGetTemplatesRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        return templatesService.getDto(id);
    }

    @DeleteMapping(value = "/templates")
    public void doDeleteTemplatesRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        templatesService.delete(id);
    }

    @PutMapping(value = "/templates")
    public void doPutTemplatesRequest(
            @RequestBody Templates templates) {

        templatesService.update(templates);
    }

    @PostMapping(value = "/templates")
    public Long doPostTemplatesRequest(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "logic_name", required = true) String logicName,
            @RequestParam(value = "default", required = true) String defValue) {

        return templatesService.create(type, logicName, defValue);
    }

    @GetMapping(value = "/type/count")
    public Long doGetTemplatesNameCountRequest() {
        return templatesService.nameCount();
    }

    @GetMapping(value = "/type/list")
    public List<PropertyType> goGetPagesTemplatesNameRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return templatesService.getPropertyTypePage(pageable);
    }

    @GetMapping(value = "/type")
    public PropertyType doGetTemplatesNameRequest(
            @RequestParam(value = "id", required = true) Long id) {

        return templatesService.getPropertyTypeDto(id);
    }

    @DeleteMapping(value = "/type")
    public void doDeleteTemplatesNameRequest(
            @RequestParam(value = "id", required = true) Long id) {

        templatesService.deletePropertyType(id);
    }

    @PutMapping(value = "/type")
    public void doPutTemplatesNameRequest(
            @RequestBody PropertyType propertyType) {

        templatesService.updatePropertyType(propertyType);
    }

    @PostMapping(value = "/type")
    public Long doPostTemplatesNameRequest(
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "logic_name", required = true) String logicName,
            @RequestParam(value = "human_name", required = true) String humanName) {

        return templatesService.createPropertyType(
                type,
                logicName,
                humanName
        );
    }
}
