package com.rental.transport.service;

import com.rental.transport.dao.TransportEntity;
import com.rental.transport.dao.TransportRepository;
import com.rental.transport.dto.Transport;
import org.springframework.stereotype.Service;

@Service
public class TransportService extends AbstractService<TransportEntity, TransportRepository, Transport> {

    public TransportService(TransportRepository repository) {
        super(repository);
    }
}
