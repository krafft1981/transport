package com.rental.transport.controller;

import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.service.TransportService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/transport")
@RestController
public class TransportController extends
        AbstractController<TransportEntity, Transport, TransportRepository, TransportMapper, TransportService> {

    public TransportController(TransportService service) {
        super(service);
    }

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportMapper mapper;

    @Override
    public Long create(Principal principal) {

        CustomerEntity customer = customerRepository.findByAccount(principal.getName());
        TransportEntity transport = new TransportEntity(customer);
        return transportRepository.save(transport).getId();
    }

    @GetMapping(value = "/list/type")
    public List<Transport> getPages(@RequestParam(value = "page", required = true) Integer page,
                                    @RequestParam(value = "size", required = true) Integer size,
                                    @RequestParam(value = "type", required = true) String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        List<Transport> transportList = transportRepository
                .findAllByType(type, pageable)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

        return transportList;
    }

    @GetMapping(value = "/count/type")
    public Long getCountByType(@RequestParam(value = "type", required = true) String type) {

        return transportRepository.findCountByType(type);
    }
}

