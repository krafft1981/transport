package com.rental.transport.service;

import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportMapper mapper;

    public void delete(@NonNull String account, @NonNull Long id) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        TransportEntity transport = transportRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Стоянка", id));

        if (transport.getCustomer().contains(customer) == false) {
            throw new AccessDeniedException("Удаление");
        }

        transportRepository.delete(transport);
    }

    public Long create(@NonNull String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        TransportEntity transport = mapper.create();
        transport.addCustomer(customer);
        return transportRepository.save(transport).getId();
    }

    public void update(@NonNull String account, @NonNull Transport dto) {

        TransportEntity transport = transportRepository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Стоянка", dto.getId()));

        transport = mapper.toEntity(dto);
        CustomerEntity customer = customerRepository.findByAccount(account);

        if (transport.getCustomer().contains(customer) == false) {
            throw new AccessDeniedException("Изменение");
        }

        transportRepository.save(transport);
    }

    public List<Transport> getPage(Pageable pageable) {

        List<Transport> result = transportRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
        return result;
    }
}
