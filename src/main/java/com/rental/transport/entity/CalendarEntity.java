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
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "calendar")
@Table(
        name="calendar",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "day_num", name = "day_num_id_idx" )
        }
)

@Setter
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Date startAt;
    private Date stopAt;
    private Long dayNum;

    private Set<OrderEntity> order = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();

    public CalendarEntity(Date startAt, Date stopAt, Long dayNum, CustomerEntity customer) {
        setStartAt(startAt);
        setStopAt(stopAt);
        setDayNum(dayNum);
        addCustomer(customer);
    }

    @Basic
    @Column(name = "start_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    public Date getStopAt() {
        return stopAt;
    }

    @Basic
    @Column(name = "day_num", nullable = false, insertable = true, updatable = true)
    public Long getDayNum() {
        return dayNum;
    }

    @ManyToMany
    @JoinTable(name="orders_calendar",
            joinColumns=@JoinColumn(name="order_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="calendar_id", nullable = false)
    )
    public Set<OrderEntity> getOrder() {
        return order;
    }

    @ManyToMany
    @JoinTable(name="customer_calendar",
            joinColumns=@JoinColumn(name="customer_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="calendar_id", nullable = false)
    )
    public Set<CustomerEntity> getCustomer() {
        return customer;
    }

    public void addCustomer(CustomerEntity entity) {
        customer.add(entity);
    }

    public void addOrder(OrderEntity entity) {
        order.add(entity);
    }
}
