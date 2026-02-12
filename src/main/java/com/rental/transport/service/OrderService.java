package com.rental.transport.service;

import com.rental.transport.entity.*;
import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    final ImageRepository imageRepository;
    final OrderRepository orderRepository;
    final CustomerRepository customerRepository;
    final TransportRepository transportRepository;
    final ParkingRepository parkingRepository;
    final TransportTypeRepository transportTypeRepository;
    final RequestRepository requestRepository;
    private final CalendarRepository calendarRepository;
    private final MessageRepository messageRepository;

    @PostConstruct
    void init() {

        final var day = 90L;
        final Integer[] hours = {10, 11, 12};

        final var ci = imageRepository.save(new ImageEntity());
        final var di = imageRepository.save(new ImageEntity());
        final var ti = imageRepository.save(new ImageEntity());
        final var pi = imageRepository.save(new ImageEntity());
        final var customer = customerRepository.save(new CustomerEntity().addImage(ci));
        final var driver = customerRepository.save(new CustomerEntity().addImage(di));
        final var parking = parkingRepository.save(new ParkingEntity().addImage(pi).addDriver(driver));
        final var type = transportTypeRepository.findByEnableTrueAndName("Яхта");
        final var calendarMessage = messageRepository.save(new MessageEntity(driver, "Выбрасываем с воре этих придурков"));
        final var calendar = calendarRepository.save(new CalendarEntity(day, hours, calendarMessage));
        final var transport = transportRepository.save(new TransportEntity()
                .setParking(Set.of(parking))
                .addImage(ti)
                .addCustomer(driver)
                .setType(type));
        var request = new RequestEntity(
                customer,
                driver,
                transport,
                calendar
        );

        request.addMessage(customer, "Хочу прийти погонять на Яхте. Свободно ?");
        request.addMessage(driver, "Да. Приходите !");

        request = requestRepository.save(request);
        final var entity = new OrderEntity(request);

        save(entity);
    }

    @Transactional
    private OrderEntity save(OrderEntity entity) {
        final var order = orderRepository.save(entity);
        entity.getRequest().setStatus(RequestStatusEnum.CONFIRMED);
        requestRepository.save(entity.getRequest());

        entity.getRequest().getCalendar().setOrderId(order.getId());
        calendarRepository.save(entity.getRequest().getCalendar());

        return order;
    }
}
