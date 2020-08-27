package com.rental.transport.controller;

import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.dto.Transport;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.service.TransportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/transport")
@RestController
public class TransportController extends
        AbstractController<TransportEntity, Transport, TransportRepository, TransportMapper, TransportService> {

    public TransportController(TransportService service) {
        super(service);
    }
}
