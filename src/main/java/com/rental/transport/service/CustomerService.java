package com.rental.transport.service;

import com.rental.transport.dto.Customer;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
public class CustomerService extends PropertyService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper mapper;

    @Value("${spring.sequrity.password}")
    private String password;

    public CustomerService() {

        setProp("name", "");
        setProp("family", "");
        setProp("last_name", "");
        setProp("phone", "");
        setProp("start_work_time", "8");
        setProp("stop_work_time", "17");
        setProp("work_at_week_end", "yes");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ObjectNotFoundException {

        CustomerEntity entity = customerRepository.findByAccount(username);
        if (Objects.isNull(entity))
            throw new ObjectNotFoundException("Account", username);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User(
                entity.getAccount(),
                password,
                Arrays.asList(authority)
        );

        return user;
    }

    public Customer create(String account) throws IllegalArgumentException {

        if (account.isEmpty())
            throw new IllegalArgumentException("Account can't be empty");

        CustomerEntity entity = customerRepository.findByAccount(account);
        if (Objects.isNull(entity)) {
            entity = new CustomerEntity(account);
            setProps(entity.getProperty());
            entity = customerRepository.save(entity);
        }

        return mapper.toDto(entity);
    }

    public Long count() {

        Long count = customerRepository.count();
        return count;
    }

    public Boolean exist(String account) {

        CustomerEntity entity = customerRepository.findByAccount(account);
        return Objects.isNull(entity) ? false : true;
    }

    public void update(String account, Customer dto)
            throws ObjectNotFoundException, AccessDeniedException {

        if (account.equals(dto.getAccount()) == false)
            throw new AccessDeniedException("Change");

        CustomerEntity entity = customerRepository.findByAccount(dto.getAccount());
        if (Objects.isNull(entity))
            throw new ObjectNotFoundException("Account", account);

        entity = mapper.toEntity(dto);

        System.out.println(entity.toString());
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
}
