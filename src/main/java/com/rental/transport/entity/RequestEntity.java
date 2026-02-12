package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractCreatableEntity;
import com.rental.transport.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "request",
        indexes = {
                @Index(columnList = "driver_id", name = "request_driver_id_idx"),
                @Index(columnList = "customer_id", name = "request_customer_id_idx"),
                @Index(columnList = "transport_id", name = "request_transport_id_idx"),
                @Index(columnList = "status", name = "request_status_idx")
        }
)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity extends AbstractCreatableEntity {

    public RequestEntity(CustomerEntity customer, CustomerEntity driver, TransportEntity transport, CalendarEntity calendar) {

        setDriver(driver);
        setCustomer(customer);
        setTransport(transport);
        setCalendar(calendar);
    }

    @OneToOne(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "calendar_id", referencedColumnName = "id", nullable = false)
    private CalendarEntity calendar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatusEnum status = RequestStatusEnum.NEW;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private CustomerEntity driver;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "transport_id", referencedColumnName = "id", nullable = false)
    private TransportEntity transport;

    @OneToOne(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private OrderEntity order;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "request_message",
            joinColumns = @JoinColumn(name = "request_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "message_id", nullable = false)
    )
    private Set<MessageEntity> message = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "request_property",
            joinColumns = @JoinColumn(name = "request_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "property_id", nullable = false)
    )
    private Set<PropertyEntity> properties = new HashSet<>();

    public RequestEntity addMessage(CustomerEntity customer, String text) {
        message.add(new MessageEntity(customer, text));
        return this;
    }
}
