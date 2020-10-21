package com.rental.transport.service;

import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.entity.TypeRepository;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransportService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private TransportMapper mapper;

    public Boolean validateDuration(Transport transport, Long duration) {

        return (duration > transport.getMinHour()) ? true : false;
    }

    public Transport get(@NonNull String account, @NonNull Long id)
            throws ObjectNotFoundException {

        TransportEntity transport = transportRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", id));

        return mapper.toDto(transport);
    }

    public TransportEntity get(Long id)
            throws ObjectNotFoundException {

        TransportEntity transport = transportRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", id));

        return transport;
    }
    public void delete(@NonNull String account, @NonNull Long id)
            throws AccessDeniedException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        TransportEntity transport = transportRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", id));

        if (transport.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Удаление");

        transport.setType(null);
        transportRepository.delete(transport);
    }

    public Long create(@NonNull String account, @NonNull String type)
            throws IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        TypeEntity typeEntity = typeRepository.findByName(type);
        if (Objects.isNull(typeEntity))
            throw new IllegalArgumentException("Неизвестный тип транспорта");

        TransportEntity transport = new TransportEntity(customer, typeEntity);
        return transportRepository.save(transport).getId();
    }

    public void update(@NonNull String account, @NonNull Transport dto)
            throws AccessDeniedException {

        TransportEntity transport = transportRepository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", dto.getId()));

        transport = mapper.toEntity(dto);
        CustomerEntity customer = customerRepository.findByAccount(account);

        if (transport.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Изменение");

        transportRepository.save(transport);
    }

    public List<Transport> getPage(Pageable pageable) {

        return transportRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public List<Transport> getPageTyped(Pageable pageable, Long type) {

        return transportRepository
                .findAllByTypeId(pageable, type)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public Long count() {

        Long count = transportRepository.count();
        return count;
    }

    public List<Transport> getMyTransport(String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return transportRepository.findAllByCustomerId(customer.getId())
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }
}
