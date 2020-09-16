package com.rental.transport.service;

import com.rental.transport.dto.Customer;
import com.rental.transport.dto.Settings;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Primary
@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper mapper;

    @Value("${spring.sequrity.password}")
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws ObjectNotFoundException {

        CustomerEntity entity = customerRepository.findByAccount(username);
        if (entity == null)
            throw new ObjectNotFoundException("Account", username);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User(
                entity.getAccount(),
                password,
                Arrays.asList(authority)
        );

        return user;
    }

    public Long create(String account) throws IllegalArgumentException {

        if (account.isEmpty())
            throw new IllegalArgumentException("Account не должен быть пустым");

        CustomerEntity entity = customerRepository.findByAccount(account);
        if (entity == null) {
            entity = new CustomerEntity(account);
            entity = customerRepository.save(entity);
        }

        return entity.getId();
    }

    public Long count() {

        Long count = customerRepository.count();
        return count;
    }

    public Boolean exist(String account) {

        CustomerEntity entity = customerRepository.findByAccount(account);
        return entity == null ? false : true;
    }

    public void update(String account, Customer dto)
            throws ObjectNotFoundException, AccessDeniedException {

        if (account.equals(dto.getAccount()) == false)
            throw new AccessDeniedException("Изменение");

        CustomerEntity entity = customerRepository.findByAccount(dto.getAccount());
        if (entity == null)
            throw new ObjectNotFoundException("Account", account);

        entity = mapper.toEntity(dto);
        customerRepository.save(entity);
    }

    public List<Customer> getPage(Pageable pageable) {

        return customerRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(customer -> { return mapper.toDto(customer); })
                .collect(Collectors.toList());
    }

    public List<Customer> findAllByIdList(Iterable<Long> ids) throws ObjectNotFoundException {

        List<Customer> result = StreamSupport
                .stream(customerRepository.findAllById(ids).spliterator(), false)
                .map(customer -> { return mapper.toDto(customer); })
                .collect(Collectors.toList());

        return result;
    }

    public Customer getMy(@NonNull String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return mapper.toDto(customer);
    }

    public List<Settings> getSettings(@NonNull String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
//        return mapper.toDto(entity);
        return null;
    }

    public void setSetting(@NonNull String account, String name, String value) {

        CustomerEntity customer = customerRepository.findByAccount(account);

    }
}
