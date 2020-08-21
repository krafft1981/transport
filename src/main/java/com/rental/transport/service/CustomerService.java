package com.rental.transport.service;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dao.CustomerRepository;
import com.rental.transport.dao.TransportEntity;
import com.rental.transport.dao.TransportRepository;
import com.rental.transport.dto.Customer;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Transport;
import com.rental.transport.utils.exceptions.CustomerNotFoundException;
import com.rental.transport.utils.exceptions.TransportNotFoundException;
import java.sql.Blob;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportRepository transportRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws CustomerNotFoundException {

        CustomerEntity entity = customerRepository.findByAccount(username);
        if (entity == null) {
            throw new CustomerNotFoundException(username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User(
                entity.getAccount(),
                "password",
                Arrays.asList(authority)
        );

        return user;
    }

    public Customer getCustomer(@NonNull String account) {

        CustomerEntity entity = customerRepository.findByAccount(account);
        if (entity == null) {
            throw new CustomerNotFoundException(account);
        }

        Customer customer = new Customer(
                entity.getId(),
                entity.getAccount(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFamily(),
                entity.getPhone(),
                entity.getImage(),
                new HashSet<>());

        entity.getTransport().stream().forEach(transport -> { customer.addTransport(transport.getId()); });
        return  customer;
    }

    public void setCustomer(@NonNull Customer customer)
            throws TransportNotFoundException {

        CustomerEntity entity = customerRepository.findByAccount(customer.getAccount());

        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setFamily(customer.getFamily());
        entity.setPhone(customer.getPhone());
        entity.setImage(customer.getImage());

        customer.getTransport().stream()
                .forEach(id -> {
                    TransportEntity transport = transportRepository
                            .findById(id)
                            .orElseThrow(() -> new TransportNotFoundException(id));
                    entity.addTransport(transport);
                });

        customerRepository.save(entity);
    }

    public Long newCustomer(@NonNull String account)
            throws CustomerNotFoundException, TransportNotFoundException {

        CustomerEntity entity = customerRepository.findByAccount(account);
        if (entity == null) {
            entity = new CustomerEntity();
            entity.setAccount(account);
            customerRepository.save(entity);
        }
        return entity.getId();
    }

    public List<Customer> getList(@NonNull Integer page, @NonNull Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return customerRepository.findAll(pageable).stream()
                .map(entity -> {
                    Customer customer = new Customer(
                            entity.getId(),
                            entity.getAccount(),
                            entity.getFirstName(),
                            entity.getLastName(),
                            entity.getFamily(),
                            entity.getPhone(),
                            entity.getImage(),
                            new HashSet<>()
                    );

//                    entity.getCustomer().stream().forEach(customer -> { transport.addDrivers(customer.getId()); });
                    return customer;
                })
                .collect(Collectors.toList());
    }
}
