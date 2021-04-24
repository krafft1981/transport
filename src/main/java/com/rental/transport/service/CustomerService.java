package com.rental.transport.service;

import com.rental.transport.dto.Customer;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
    private EmailService emailService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PropertyService propertyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(username);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        User user = new User(
                entity.getAccount(),
                entity.getPassword(),
                Arrays.asList(authority)
        );

        return user;
    }

    public Customer create(String account, String password, String phone, String fio) throws IllegalArgumentException {

        if (account.isEmpty())
            throw new IllegalArgumentException("Account can't be empty");

        if (Objects.nonNull(customerRepository.findByEnableTrueAndConfirmedTrueAndAccount(account))) {
            throw new IllegalArgumentException("account already exists");
        }

        CustomerEntity customer = new CustomerEntity(account, password);
        customer.addProperty(
                propertyService.create("customer_fio", fio),
                propertyService.create("customer_phone", phone),
                propertyService.create("customer_startWorkTime", "9"),
                propertyService.create("customer_stopWorkTime", "20"),
                propertyService.create("customer_workAtWeekEnd", "Да"),
                propertyService.create("customer_description", "Не указано")
        );

        customerRepository.save(customer);
        emailService.sendVerifyEmail(customer);
        return customerMapper.toDto(customer);
    }

    public void confirm(String account) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);
        entity.setConfirmed();
        customerRepository.save(entity);
    }

    public void update(String account, Customer dto)
            throws ObjectNotFoundException, AccessDeniedException {

        if (!account.equals(dto.getAccount()))
            throw new AccessDeniedException("Change");

        CustomerEntity entityFromDb = getEntity(account);
        CustomerEntity entity = customerMapper.toEntity(dto);
        entity.setPassword(entityFromDb.getPassword());
        customerRepository.save(entity);
    }

    public void updatePassword(String account, String password)
            throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);
        entity.setPassword(password);
        customerRepository.save(entity);
    }

    public List<Customer> getPage(Pageable pageable) {

        return customerRepository
                .findAllByEnableTrueAndConfirmedTrue(pageable)
                .stream()
                .map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
    }

    public CustomerEntity getEntity(String account) throws ObjectNotFoundException {

        CustomerEntity entity = customerRepository.findByEnableTrueAndConfirmedTrueAndAccount(account);
        if (Objects.isNull(entity))
            throw new ObjectNotFoundException("Account", account);

        if (!entity.getConfirmed()) {
            throw new ObjectNotFoundException("Account", account);
        }

        return entity;
    }

    public Customer getDto(String account) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);
        return customerMapper.toDto(entity);
    }

    public void check(String account) throws ObjectNotFoundException {
        CustomerEntity customer = customerRepository.findByEnableTrueAndConfirmedTrueAndAccount(account);
        emailService.sendVerifyEmail(customer);
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("customer_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("customer_phone", "Сотовый", PropertyTypeEnum.Phone);
        propertyService.createType("customer_startWorkTime", "Час начала работы", PropertyTypeEnum.Hour);
        propertyService.createType("customer_stopWorkTime", "Час окончания работы", PropertyTypeEnum.Hour);
        propertyService.createType("customer_workAtWeekEnd", "Работает в выходные", PropertyTypeEnum.Boolean);
        propertyService.createType("customer_description", "Описание", PropertyTypeEnum.String);
    }
}
