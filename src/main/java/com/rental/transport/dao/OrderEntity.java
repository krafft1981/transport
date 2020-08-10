package com.rental.transport.dao;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name="orders", schema = "public", catalog = "relationship")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends EntityId  {

    private Long customer;
    private Long transport;
    private Long driver;
    private Date startAt;
    private Date stopAt;
    private float price;
    private Byte count;
    private String comment;

    @Basic
    @Column(name = "customer_id", nullable = false, insertable = true, updatable = false)
    public Long getCustomer() {
        return customer;
    }

    @Basic
    @Column(name = "transport_id", nullable = false, insertable = true, updatable = false)
    public Long getTransport() {
        return transport;
    }

    @Basic
    @Column(name = "driver_id", nullable = false, insertable = true, updatable = false)
    public Long getDriver() {
        return driver;
    }

    @Basic
    @Column(name = "start_at", columnDefinition = "timestamp with time zone not null")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", columnDefinition = "timestamp with time zone not null")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStopAt() {
        return stopAt;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = false)
    public float getPrice() {
        return price;
    }

    @Basic
    @Column(name = "count", nullable = false, insertable = true, updatable = false)
    public float getCount() {
        return count;
    }

    @Basic
    @Column(name = "comment", nullable = true, insertable = true, updatable = false)
    @Type(type="text")
    public String getComment() {
        return comment;
    }
}
