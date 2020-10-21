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
                @Index(columnList = "transport_id", name = "order_transport_idx"),
                @Index(columnList = "state", name = "order_state_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends AbstractEntity  {

    private String customerName = "";
    private String customerPhone = "";
    private CustomerEntity customer;

    private TransportEntity transport;

    private Set<CustomerEntity> driver = new HashSet<>();

    private Double latitude = 0.0;
    private Double longitude = 0.0;

//    private Set<CalendarEntity> calendar = new HashSet<>();

    private Integer confirmed = 0;

    private Date createdAt = new Date();

    private Double cost = 0.0;
    private Double price = 0.0;

    private String comment = "";
    private String state = "New";

//    public void addCalendar(CalendarEntity entity) {
//        this.calendar.add(entity);
//    }

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

    @ManyToMany
    @JoinTable(name="orders_driver",
            joinColumns=@JoinColumn(name="order_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="customer_id", nullable = false)
    )
    public Set<CustomerEntity> getDriver() {
        return driver;
    }

    public void addDriver(CustomerEntity entity) {
        driver.add(entity);
    }

    @ManyToOne
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    public TransportEntity getTransport() {
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
    @Column(name = "confirmed", nullable = false, insertable = true, updatable = true)
    public Integer getConfirmed() {
        return confirmed;
    }

//    @OneToMany
//    public Set<CalendarEntity> getCalendar() {
//        return calendar;
//    }

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
    @Column(name = "price", nullable = false, insertable = true, updatable = false)
    public Double getPrice() {
        return price;
    }

    @Basic
    @Column(name = "comment", nullable = false, insertable = true, updatable = true)
    @Type(type="text")
    public String getComment() {
        return comment;
    }

    @Basic
    @Column(name = "state", nullable = false, insertable = true, updatable = true)
    public String getState() {
        return state;
    }
}
