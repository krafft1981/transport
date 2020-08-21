package com.rental.transport.service;

import com.rental.transport.dao.OrderRepository;
import com.rental.transport.dto.Order;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    public Order findById(@NonNull String account, @NonNull Long id) {

        return null;
    }

    public List<Order> getList(@NonNull Integer page, @NonNull Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return null;
    }

    public void setOrder(@NonNull Order order) {

    }

    public Long newOrder(@NonNull String account) {
        return 0L;
    }
}
