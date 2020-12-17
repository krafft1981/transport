package com.rental.transport.service;

import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.entity.TypeEntity;
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
    private CustomerService customerService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TransportMapper mapper;

    public void delete(@NonNull String account, @NonNull Long id)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.get(account);
        TransportEntity transport = get(id);

        if (transport.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        transport.setType(null);
        transportRepository.delete(transport);
    }

    public Long create(@NonNull String account, @NonNull String type)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.get(account);
        TypeEntity typeEntity = typeService.get(type);
        TransportEntity entity = new TransportEntity(customer, typeEntity);
        return transportRepository.save(entity).getId();
    }

    public void update(@NonNull String account, @NonNull Transport dto)
            throws AccessDeniedException, ObjectNotFoundException {

        TransportEntity entity = get(dto.getId());
        entity = mapper.toEntity(dto);
        CustomerEntity customer = customerService.get(account);

        if (!entity.getCustomer().contains(customer))
            throw new AccessDeniedException("Change");

        entity.addPropertyList();
        transportRepository.save(entity);
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

        CustomerEntity customer = customerService.get(account);
        return transportRepository.findAllByCustomerId(customer.getId())
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public TransportEntity get(Long id) throws ObjectNotFoundException {

        return transportRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Transport", id));
    }
}
