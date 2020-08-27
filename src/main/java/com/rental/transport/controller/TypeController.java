package com.rental.transport.controller;

import com.rental.transport.entity.TypeEntity;
import com.rental.transport.entity.TypeRepository;
import com.rental.transport.dto.Type;
import com.rental.transport.mapper.TypeMapper;
import com.rental.transport.service.TypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/type")
@RestController
public class TypeController
        extends AbstractController<TypeEntity, Type, TypeRepository, TypeMapper, TypeService> {

    public TypeController(TypeService service) {
        super(service);
    }
}
