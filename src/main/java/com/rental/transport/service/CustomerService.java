package com.rental.transport.service;

import com.rental.transport.dto.Customer;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Primary
@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PropertyService propertyService;

    private ValidatorFactory vf = new ValidatorFactory();

    @Override
    public UserDetails loadUserByUsername(String account) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_TRANSPORT");
        return new User(
                entity.getAccount(),
                entity.getPassword(),
                Collections.singletonList(authority)
        );
    }

    @Transactional
    public Customer create(String account, String password, String phone, String fio, String tz)
            throws IllegalArgumentException {

        account = account.toLowerCase();

        if (Objects.nonNull(customerRepository.findByAccountAndEnableTrueAndConfirmedTrue(account)))
            throw new IllegalArgumentException("Учётная запись уже существует");

        if (!vf.getValidator("Email").validate(account))
            throw new IllegalArgumentException("Неправильное значение поля 'почта'");

        if (password.length() < 4)
            throw new IllegalArgumentException("Пароль должен быть не короче 4х символов");

        if (!vf.getValidator("Phone").validate(phone))
            throw new IllegalArgumentException("Неправильное значение поля 'телефон'");

        if (!vf.getValidator("String").validate(fio))
            throw new IllegalArgumentException("Неправильное значение поля 'имя'");

        CustomerEntity customer = new CustomerEntity(account, password, tz);
        customer.addProperty(
                propertyService.create("customer_fio", fio),
                propertyService.create("customer_phone", phone),
                propertyService.create("customer_startWorkTime", "9"),
                propertyService.create("customer_stopWorkTime", "20"),
                propertyService.create("customer_workAtWeekEnd", "Да"),
                propertyService.create("customer_description", "Не указано"),
                propertyService.create("customer_request_duration", "60"),
                propertyService.create("customer_card_number", "Не указано")
        );

        customerRepository.save(customer);
        if (customer.getSendEmail())
            emailService.sendVerifyEmail(customer);

        return customerMapper.toDto(customer);
    }

    public void confirm(String account) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);
        entity.setConfirmed();
        customerRepository.save(entity);
    }

    public Customer update(String account, Customer customer)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        account = account.toLowerCase();

        if (!account.equals(customer.getAccount()))
            throw new AccessDeniedException("Change");

        for (Property property : customer.getProperty()) {
            if (!vf.getValidator(property.getType()).validate(property.getValue()))
                throw new IllegalArgumentException("Неправильное значение поля: '" + property.getHumanName() + "'");
        }

        CustomerEntity entityFromDb = getEntity(account);
        CustomerEntity entity = customerMapper.toEntity(customer);
        entity.setPassword(entityFromDb.getPassword());
        customerRepository.save(entity);
        return customerMapper.toDto(entity);
    }

    public Customer updatePassword(String account, String password)
            throws ObjectNotFoundException, IllegalArgumentException {

        if (!vf.getValidator("Password").validate(password))
            throw new IllegalArgumentException("Ошибка в поле пароль");

        CustomerEntity customer = getEntity(account);
        customer.setPassword(password);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public List<Customer> getPage(Pageable pageable) {

        return customerRepository
                .findAllByEnableTrueAndConfirmedTrue(pageable)
                .parallelStream()
                .map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
    }

    public CustomerEntity getEntity(String account) throws ObjectNotFoundException {

        account = account.toLowerCase();

        CustomerEntity entity = customerRepository.findByAccountAndEnableTrueAndConfirmedTrue(account);
        if (Objects.isNull(entity))
            throw new ObjectNotFoundException("Account", account);

        if (!entity.getConfirmed())
            throw new ObjectNotFoundException("Account", account);

        return entity;
    }

    public Customer getDto(String account) throws ObjectNotFoundException {

        CustomerEntity entity = getEntity(account);
        return customerMapper.toDto(entity);
    }

    public void check(String account) throws ObjectNotFoundException {

        account = account.toLowerCase();

        CustomerEntity customer = customerRepository.findByAccountAndEnableTrueAndConfirmedTrue(account);
        emailService.sendVerifyEmail(customer);
    }

    @Transactional
    public Customer addCustomerImage(String account, byte[] data) throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = getEntity(account);
        ImageEntity image = new ImageEntity(data);
        customer.addImage(image);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Transactional
    public Customer delCustomerImage(String account, Long imageId) throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = getEntity(account);
        ImageEntity image = imageService.getEntity(imageId);
        customer.delImage(image);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Transactional
    @PostConstruct
    public void postConstruct() {

        propertyService.createType("customer_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("customer_phone", "Сотовый", PropertyTypeEnum.Phone);
        propertyService.createType("customer_startWorkTime", "Час начала работы", PropertyTypeEnum.Hour);
        propertyService.createType("customer_stopWorkTime", "Час окончания работы", PropertyTypeEnum.Hour);
        propertyService.createType("customer_workAtWeekEnd", "Работает в выходные", PropertyTypeEnum.Boolean);
        propertyService.createType("customer_description", "Описание", PropertyTypeEnum.String);
        propertyService.createType("customer_request_duration", "Продолжительность запроса(минут)", PropertyTypeEnum.Integer);
        propertyService.createType("customer_card_number", "Номер карты", PropertyTypeEnum.String);
    }
}
