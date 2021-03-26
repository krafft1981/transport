package com.rental.transport.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "request",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "driver_id", name = "request_driver_id_idx"),
                @Index(columnList = "customer_id", name = "request_customer_id_idx"),
                @Index(columnList = "transport_id", name = "request_transport_id_idx")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"customer_id", "transport_id", "calendar_id"})
        }
)

@Setter
@NoArgsConstructor
public class RequestEntity extends AbstractEntity {

    private Date createdAt = new Date();
    private Date interactAt = null;

    private Long order;

    private CustomerEntity driver;
    private TransportEntity transport;
    private CalendarEntity calendar;
    private CustomerEntity customer;

    public RequestEntity(CustomerEntity customer, CustomerEntity driver, TransportEntity transport, CalendarEntity calendar) {

        setDriver(driver);
        setCustomer(customer);
        setCalendar(calendar);
        setTransport(transport);
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
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    public CustomerEntity getDriver() {
        return driver;
    }

    @Basic
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    public TransportEntity getTransport() {
        return transport;
    }

    @Basic
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "calendar_id", referencedColumnName = "id")
    public CalendarEntity getCalendar() {
        return calendar;
    }

    @Basic
    @Column(name = "order_id", nullable = true, insertable = true, updatable = true)
    public Long getOrder() {
        return order;
    }

    public void setInteract() {
        setInteractAt(new Date());
    }
}
