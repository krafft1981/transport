package com.rental.transport.service;

import com.rental.transport.entity.ConfirmationEntity;
import com.rental.transport.entity.ConfirmationRepository;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Long> getByAccount(String account) {

        return null;
    }

    public ConfirmationEntity get(Long id) throws ObjectNotFoundException {

        ConfirmationEntity entity = confirmationRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Confirmation", id));
        return entity;
    }
}
