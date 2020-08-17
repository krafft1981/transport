package com.rental.transport.service;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dao.CustomerRepository;
import com.rental.transport.dao.TransportEntity;
import com.rental.transport.dao.TransportRepository;
import com.rental.transport.dto.Transport;
import com.rental.transport.utils.exceptions.CustomerNotFoundException;
import com.rental.transport.utils.exceptions.TransportNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Transport findById(@NonNull Long id) throws TransportNotFoundException {

        TransportEntity entity = transportRepository
                .findById(id)
                .orElseThrow(() -> new TransportNotFoundException(id));

        Transport transport = new Transport(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getImage(),
                entity.getCapacity(),
                entity.getDescription(),
                new HashSet<>()
        );

        entity.getCustomer().stream().forEach(customer -> { transport.addDrivers(customer.getId()); });
        return transport;
    }

    public Long newTransport(@NonNull String account, @NonNull String type) {

        TransportEntity entity = new TransportEntity();
        entity.setType(type);
        entity.addCustomer(customerRepository.findByAccount(account));
        entity = transportRepository.save(entity);
        return entity.getId();
    }

    public void deleteTransport(@NonNull String account, @NonNull Long id) throws TransportNotFoundException {

        TransportEntity transportEntity = transportRepository
                .findById(id)
                .orElseThrow(() -> new TransportNotFoundException(id));

        CustomerEntity customerEntity = customerRepository.findByAccount(account);

        transportEntity.getCustomer().stream().forEach(entity -> {
            if (entity.getId().equals(customerEntity.getId())) {
                transportRepository.delete(transportEntity);
            }
        });
    }

    public void updateTransport(@NonNull String account, @NonNull Transport transport)
            throws TransportNotFoundException, CustomerNotFoundException {

        TransportEntity transportEntity = transportRepository
                .findById(transport.getId())
                .orElseThrow(() -> new TransportNotFoundException(transport.getId()));

        CustomerEntity customerEntity = customerRepository.findByAccount(account);

        transportEntity.getCustomer().stream().forEach(entity -> {
            if (entity.getId().equals(customerEntity.getId())) {
                transportEntity.setCapacity(transport.getCapacity());
                transportEntity.setDescription(transport.getDescription());
                transportEntity.setName(transport.getName());
                transportEntity.setImage(transport.getImage());
                transport.getDrivers().stream()
                        .forEach(driver -> {
                            CustomerEntity customer = customerRepository
                                    .findById(driver)
                                    .orElseThrow(() -> new CustomerNotFoundException(driver));
                            transportEntity.addCustomer(customer);
                        });
                transportRepository.save(transportEntity);
            }
        });
    }

    public List<Transport> getMy(@NonNull String account) {

        List<TransportEntity> entityList = null; //transportRepository.findByAccount(account);
        return entityList.stream()
                .map(entity -> {
                    Transport transport = new Transport(
                            entity.getId(),
                            entity.getName(),
                            entity.getType(),
                            entity.getImage(),
                            entity.getCapacity(),
                            entity.getDescription(),
                            new HashSet<>()
                    );

                    entity.getCustomer().stream().forEach(customer -> { transport.addDrivers(customer.getId()); });
                    return transport;
                })
                .collect(Collectors.toList());
    }

    public List<Transport> getList(@NonNull Integer page, @NonNull Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return transportRepository.findAll(pageable).stream()
                .map(entity -> {
                    Transport transport = new Transport(
                            entity.getId(),
                            entity.getName(),
                            entity.getType(),
                            entity.getImage(),
                            entity.getCapacity(),
                            entity.getDescription(),
                            new HashSet<>()
                    );

                    entity.getCustomer().stream().forEach(customer -> { transport.addDrivers(customer.getId()); });
                    return transport;
                })
                .collect(Collectors.toList());
    }
}
