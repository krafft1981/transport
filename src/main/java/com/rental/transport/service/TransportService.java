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
    private ParkingService parkingService;

    @Autowired
    private TemplatesService templatesService;

    @Autowired
    private TransportMapper transportMapper;

    public void delete(@NonNull String account, @NonNull Long id)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = getEntity(id);

        if (transport.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        transport.setType(null);
        transportRepository.delete(transport);
    }

    public Long create(@NonNull String account, @NonNull String type)
            throws ObjectNotFoundException {

        CustomerEntity customerEntity = customerService.getEntity(account);
        TypeEntity typeEntity = typeService.getEntity(type);
        TransportEntity transport = new TransportEntity(customerEntity, typeEntity);
        transport.setProperty(templatesService.getTypeProperty(typeEntity));
        return transportRepository.save(transport).getId();
    }

    public void update(@NonNull String account, @NonNull Transport dto)
            throws AccessDeniedException, ObjectNotFoundException {

        TransportEntity entity = transportMapper.toEntity(dto);
        CustomerEntity customer = customerService.getEntity(account);

        if (!entity.getCustomer().contains(customer))
            throw new AccessDeniedException("Change");

        transportRepository.save(entity);
    }

    public List<Transport> getPage(Pageable pageable) {

        return transportRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .map(entity -> {
                    return transportMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public List<Transport> getPageTyped(Pageable pageable, Long type) {

        return transportRepository
                .findAllByEnableTrueAndTypeId(pageable, type)
                .stream()
                .filter(entity -> entity.getType().getEnable())
                .map(entity -> {
                    return transportMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public Long count() {

        return transportRepository.count();
    }

    public List<Transport> getMyTransport(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return transportRepository.findAllByCustomerId(customer.getId())
                .stream()
                .map(entity -> {
                    return transportMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public TransportEntity getEntity(Long id) throws ObjectNotFoundException {

        return transportRepository
                .findById(id)
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .orElseThrow(() -> new ObjectNotFoundException("Transport", id));
    }

    public Transport getDto(Long id) throws ObjectNotFoundException {

        return transportMapper.toDto(getEntity(id));
    }

    public List<Transport> getParkingTransport(Long parkingId) throws ObjectNotFoundException {

        return parkingService.getEntity(parkingId)
                .getTransport()
                .stream()
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .map(transport -> {
                    return transportMapper.toDto(transport);
                })
                .collect(Collectors.toList());
    }
}
