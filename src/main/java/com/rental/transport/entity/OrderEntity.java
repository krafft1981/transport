package com.rental.transport.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity(name = "orders")
@Table(
        name="orders",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "order_customer_idx"),
                @Index(columnList = "driver_id", name = "order_driver_idx"),
                @Index(columnList = "transport_id", name = "order_transport_idx"),
                @Index(columnList = "state", name = "order_state_idx"),
                @Index(columnList = "start_at", name = "order_start_at_idx"),
                @Index(columnList = "stop_at", name = "order_stop_at_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends AbstractEntity  {

    private String customerName = "";
    private String customerPhone = "";
    private CustomerEntity customer;

    private Long transport;

    private String driverName = "";
    private String driverPhone = "";
    private Long driver;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private Date startAt;
    private Date stopAt;

    private Date createdAt = new Date();

    private Double cost = 0.0;
    private Double price = 0.0;

    private String comment = "";
    private String state = "New";

    @Basic
    @Column(name = "customer_name", nullable = false, insertable = true, updatable = true)
    public String getCustomerName() {
        return customerName;
    }

    @Basic
    @Column(name = "customer_phone", nullable = false, insertable = true, updatable = true)
    public String getCustomerPhone() {
        return customerPhone;
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @Column(name = "driver_name", nullable = true, insertable = true, updatable = true)
    public String getDriverName() {
        return driverName;
    }

    @Basic
    @Column(name = "driver_phone", nullable = true, insertable = true, updatable = true)
    public String getDriverPhone() {
        return driverPhone;
    }

    @Basic
    @Column(name = "driver_id", nullable = true, insertable = true, updatable = true)
    public Long getDriver() {
        return driver;
    }

    @Basic
    @Column(name = "transport_id", nullable = false, insertable = true, updatable = true)
    public Long getTransport() {
        return transport;
    }

    @Basic
    @Column(name = "latitude", nullable = true, insertable = true, updatable = true)
    public Double getLatitude() {
        return latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = true, insertable = true, updatable = true)
    public Double getLongitude() {
        return longitude;
    }

    @Basic
    @Column(name = "start_at", nullable = true, columnDefinition = "timestamp with time zone")
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", nullable = true, columnDefinition = "timestamp with time zone")
    public Date getStopAt() {
        return stopAt;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getCreatedAt() {
        return createdAt;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = false)
    public Double getCost() {
        return cost;
    }

    @Basic
    @Column(name = "price", nullable = true, insertable = true, updatable = false)
    public Double getPrice() {
        return price;
    }

    @Basic
    @Column(name = "comment", nullable = true, insertable = true, updatable = false)
    @Type(type="text")
    public String getComment() {
        return comment;
    }

    @Basic
    @Column(name = "state", nullable = false, insertable = true, updatable = false)
    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("OrderEntity{");
        sb.append("startAt=").append(startAt);
        sb.append(", stopAt=").append(stopAt);
        sb.append('}');
        return sb.toString();
    }
}
