package com.rental.transport.service;

import com.rental.transport.dto.Customer;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.enums.ErrorTypeEnum;
import com.rental.transport.enums.PropertyNameEnum;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final ImageService imageService;
    private final CustomerMapper customerMapper;
    private final PropertyService propertyService;
    private final ValidatorFactory vf;

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
            throw new IllegalArgumentException(ErrorTypeEnum.CUSTOMER_ALREADY_EXIST.getName());

        if (!vf.getValidator(PropertyTypeEnum.EMAIL).validate(account))
            throw new IllegalArgumentException(ErrorTypeEnum.CUSTOMER_EMAIL_WRONG_FIELD_MAIL.name());

        if (password.length() < 4)
            throw new IllegalArgumentException(ErrorTypeEnum.CUSTOMER_EMAIL_WRONG_FIELD_PASSWORD_LENGTH.name());

        if (!vf.getValidator(PropertyTypeEnum.PHONE).validate(phone))
            throw new IllegalArgumentException(ErrorTypeEnum.CUSTOMER_EMAIL_WRONG_FIELD_PHONE.name());

        if (!vf.getValidator(PropertyTypeEnum.STRING).validate(fio))
            throw new IllegalArgumentException(ErrorTypeEnum.CUSTOMER_EMAIL_WRONG_FIELD_NAME.name());

        CustomerEntity customer = new CustomerEntity(account, password, tz);
        customer.addProperty(
            propertyService.create(PropertyNameEnum.CUSTOMER_NAME, fio),
            propertyService.create(PropertyNameEnum.CUSTOMER_PHONE, phone),
            propertyService.create(PropertyNameEnum.CUSTOMER_START_WORK_TIME, "8"),
            propertyService.create(PropertyNameEnum.CUSTOMER_STOP_WORK_TIME, "20"),
            propertyService.create(PropertyNameEnum.CUSTOMER_WORK_AT_WEEK_END, "Да"),
            propertyService.create(PropertyNameEnum.CUSTOMER_DESCRIPTION, "Не указано"),
            propertyService.create(PropertyNameEnum.CUSTOMER_REQUEST_DURATION, "60"),
            propertyService.create(PropertyNameEnum.CUSTOMER_CARD_NUMBER, "5469-5400-1711-8918"),
            propertyService.create(PropertyNameEnum.CUSTOMER_CARD_TYPE, "Сбербанк")
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
            if (!vf.getValidator(PropertyTypeEnum.valueOf(property.getType())).validate(property.getValue()))
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

        if (!vf.getValidator(PropertyTypeEnum.PASSWORD).validate(password))
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
                   .map(customerMapper::toDto)
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
}
