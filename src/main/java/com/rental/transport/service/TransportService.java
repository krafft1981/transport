package com.rental.transport.service;

import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.dto.Transport;
import com.rental.transport.mapper.TransportMapper;
import org.springframework.stereotype.Service;

@Service
public class TransportService extends AbstractService<TransportEntity, TransportRepository, TransportMapper, Transport> {

    public TransportService(TransportRepository repository, TransportMapper mapper) {
        super(repository, mapper);
    }
}
