package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity(name = "orders")
@Table(
        name = "orders",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "order_customer_idx"),
                @Index(columnList = "driver_id", name = "order_driver_idx"),
                @Index(columnList = "transport_id", name = "order_transport_idx")
        }
)

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends AbstractEntity {

    private CustomerEntity customer;
    private CustomerEntity driver;
    private TransportEntity transport;

    private Set<PropertyEntity> property = new HashSet();
    private Set<MessageEntity> message = new HashSet();
    private Long day;
    private Integer[] hours;

    public OrderEntity(CustomerEntity customer, TransportEntity transport, CustomerEntity driver, Long day, Integer[] hours) {

        setCustomer(customer);
        setTransport(transport);
        setDay(day);
        setHours(hours);
        setDriver(driver);
    }

    @Basic
    @Column(name = "hours", columnDefinition = "Integer[]", nullable = false)
    @Type(type = "int-array")
    public Integer[] getHours() {
        return hours;
    }

    @Basic
    @Column(name = "day", nullable = false, insertable = true, updatable = true)
    public Long getDay() {
        return day;
    }

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

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    public CustomerEntity getDriver() {
        return driver;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "orders_message",
            joinColumns = @JoinColumn(name = "orders_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "message_id", nullable = false)
    )
    public Set<MessageEntity> getMessage() {
        return message;
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

        for (PropertyEntity property : entryes)
            addProperty(property);
    }

    public void addMessage(MessageEntity entity) {
        message.add(entity);
    }
}
