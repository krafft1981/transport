package com.rental.transport.entity;

import java.util.Currency;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(
        name="orders",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "driver_id", name = "driver_id_idx"),
                @Index(columnList = "customer_id", name = "customer_id_idx"),
                @Index(columnList = "transport_id", name = "transport_id_idx"),
                @Index(columnList = "start_at", name = "start_id_idx"),
                @Index(columnList = "stop_at", name = "stop_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends AbstractEntity  {

    private Long customerId;
    private Long transportId;
    private Long driverId;
    private Date startAt;
    private Date stopAt;
    private Date createdAt;
    private Currency price;
    private String comment;

    @Basic
    @Column(name = "customer_id", nullable = false, insertable = true, updatable = false)
    public Long getCustomerId() {
        return customerId;
    }

    @Basic
    @Column(name = "transport_id", nullable = false, insertable = true, updatable = false)
    public Long getTransportId() {
        return transportId;
    }

    @Basic
    @Column(name = "driver_id", nullable = false, insertable = true, updatable = false)
    public Long getDriverId() {
        return driverId;
    }

    @Basic
    @Column(name = "start_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStopAt() {
        return stopAt;
    }

    @Basic
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP not null")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Column(name = "price", nullable = true, insertable = true, updatable = false)
    public Currency getPrice() {
        return price;
    }

    @Basic
    @Column(name = "comment", nullable = true, insertable = true, updatable = false)
    @Type(type="text")
    public String getComment() {
        return comment;
    }
}
