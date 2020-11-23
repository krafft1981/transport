package com.rental.transport.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "orders")
@Table(
        name="orders",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "order_customer_idx"),
                @Index(columnList = "transport_id", name = "order_transport_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends AbstractEntity  {

    private CustomerEntity customer;
    private TransportEntity transport;

    private Set<PropertyEntity> property = new HashSet<>();
    private Set<CustomerEntity> driver = new HashSet<>();
    private Set<CalendarEntity> calendar = new HashSet<>();
    private Set<MessageEntity> message = new HashSet<>();

    private Date createdAt = new Date();

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @ManyToOne
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    public TransportEntity getTransport() {
        return transport;
    }

    @OneToMany
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    @ManyToMany
    @JoinTable(name="orders_driver",
            joinColumns=@JoinColumn(name="order_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="customer_id", nullable = false)
    )
    public Set<CustomerEntity> getDriver() {
        return driver;
    }

    @OneToMany
    public Set<CalendarEntity> getCalendar() {
        return calendar;
    }

    @OneToMany
    public Set<MessageEntity> getMessage() {
        return message;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getCreatedAt() {
        return createdAt;
    }
}
