package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderMapper mapper;

    public List<Order> getPage(@NonNull String account, Pageable pageable) {

        return orderRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public Long create(@NonNull String account, @NonNull Long transport_id)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        TransportEntity transport = transportRepository.findById(transport_id).orElseThrow(() -> new ObjectNotFoundException("Транспорт", transport_id));
        OrderEntity order = new OrderEntity( customer, transport );
        return orderRepository.save(order).getId();
    }

    public Long count() {

        Long count = orderRepository.count();
        return count;
    }
}
