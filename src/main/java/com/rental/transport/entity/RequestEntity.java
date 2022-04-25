package com.rental.transport.entity;

import com.rental.transport.enums.RequestStatusEnum;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity(name = "request")
@Table(
    name = "request",
    schema = "public",
    catalog = "relationship",
    indexes = {
        @Index(columnList = "driver_id", name = "request_driver_id_idx"),
        @Index(columnList = "customer_id", name = "request_customer_id_idx"),
        @Index(columnList = "transport_id", name = "request_transport_id_idx"),
        @Index(columnList = "status", name = "request_status_idx"),
        @Index(columnList = "day", name = "request_day_idx")
    }
)

@Setter
@NoArgsConstructor
public class RequestEntity extends AbstractEntity {

    private Date createdAt = currentTime();
    private Date interactAt = null;

    private Long day;
    private Integer[] hours;

    private Long order;
    private RequestStatusEnum status = RequestStatusEnum.NEW;

    private CustomerEntity driver;
    private TransportEntity transport;
    private CustomerEntity customer;

    public RequestEntity(CustomerEntity customer, CustomerEntity driver, TransportEntity transport, Long day, Integer[] hours) {

        setDriver(driver);
        setCustomer(customer);
        setTransport(transport);
        setDay(day);
        setHours(hours);
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

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "interact_at", nullable = true, columnDefinition = "timestamp with time zone")
    public Date getInteractAt() {
        return interactAt;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, insertable = true, updatable = true)
    public RequestStatusEnum getStatus() {
        return status;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    public CustomerEntity getDriver() {
        return driver;
    }

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    public TransportEntity getTransport() {
        return transport;
    }

    @Basic
    @Column(name = "order_id", nullable = true, insertable = true, updatable = true)
    public Long getOrder() {
        return order;
    }

    public void setInteract(RequestStatusEnum status) {
        setInteractAt(currentTime());
        setStatus(status);
    }
}
