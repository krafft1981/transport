package com.rental.transport.service;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dao.CustomerRepository;
import com.rental.transport.dto.Customer;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CustomerService extends AbstractService<CustomerEntity, CustomerRepository, Customer> implements UserDetailsService {

    public CustomerService(CustomerRepository repository) {
        super(repository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ObjectNotFoundException {

        CustomerEntity entity = repository.findByAccount(username);
        if (entity == null) {
            throw new ObjectNotFoundException("Customer", username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User(
                entity.getAccount(),
                "password",
                Arrays.asList(authority)
        );

        return user;
    }

    public Long create(String account) throws IllegalArgumentException {

        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account не должен быть пустым");
        }

        CustomerEntity entity = repository.findByAccount(account);
        if (entity == null) {
            entity = new CustomerEntity(account);
            entity = repository.save(entity);
        }

        return entity.getId();
    }
}
