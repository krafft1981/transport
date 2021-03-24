package com.rental.transport.entity;

import com.rental.transport.enums.OrderStatusEnum;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        name = "orders",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "order_customer_idx"),
                @Index(columnList = "transport_id", name = "order_transport_idx")
        }
)

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends AbstractEntity {

    private OrderStatusEnum status = OrderStatusEnum.New;

    private CustomerEntity customer;
    private TransportEntity transport;

    private Set<PropertyEntity> property = new HashSet<>();
    private Set<CustomerEntity> driver = new HashSet<>();
    private Set<CalendarEntity> calendar = new HashSet<>();
    private Set<MessageEntity> message = new HashSet<>();

    private Date createdAt = new Date();
    private Date confirmedAt = null;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public OrderStatusEnum getStatus() {
        return status;
    }

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

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    @ManyToMany
    @JoinTable(name = "orders_driver",
            joinColumns = @JoinColumn(name = "order_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false)
    )
    public Set<CustomerEntity> getDriver() {
        return driver;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "orders_calendar",
            joinColumns = @JoinColumn(name = "orders_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "calendar_id", nullable = false)
    )
    public Set<CalendarEntity> getCalendar() {
        return calendar;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "orders_message",
            joinColumns = @JoinColumn(name = "orders_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "message_id", nullable = false)
    )    public Set<MessageEntity> getMessage() {
        return message;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirmed_at", nullable = false, columnDefinition = "timestamp with time zone")
    public Date getConfirmedAt() {
        return confirmedAt;
    }

    public void addProperty(PropertyEntity entity) {

        String name = entity.getType().getLogicName();

        entity = property.stream()
                .filter(propertyEntity -> propertyEntity.getType().getLogicName().equals(name))
                .findFirst()
                .orElse(entity);

        property.add(entity);
    }

    public void addProperty(PropertyEntity... entryes) {

        for (int id = 0; id < entryes.length; id++)
            addProperty(entryes[id]);
    }

    public void addDriver(CustomerEntity entity) {
        driver.add(entity);
    }

    public void addMessage(MessageEntity entity) {
        message.add(entity);
    }

    public void addCalendar(CalendarEntity entity) {
        calendar.add(entity);
    }

    public void deleteCalendarEntity(CalendarEntity entity) {

        calendar.remove(entity);
    }
}
