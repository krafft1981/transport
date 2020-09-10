package com.rental.transport.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
/*
                @Index(columnList = "driver_id", name = "order_driver_idx"),
                @Index(columnList = "customer_id", name = "order_customer_idx"),
                @Index(columnList = "transport_id", name = "order_transport_idx"),
                @Index(columnList = "start_at", name = "order_start_idx"),
                @Index(columnList = "stop_at", name = "order_stop_idx"),
                @Index(columnList = "created_at", name = "order_created_idx")
*/
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends AbstractEntity  {

    private String customerFio = "";
    private String customerPhone = "";
    private Long customerId;

    private String driverFio = "";
    private String driverPhone = "";
    private Long driverId;

    private Long transportId;

    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private Date startAt;
    private Date stopAt;

    private Date createdAt = new Date();

    private Double cost = 0.0;
    private Double price = 0.0;

    private String comment = "";
    private String status = "";

    public OrderEntity(CustomerEntity customer, TransportEntity transport) {
        customerFio = customer.getFamily() + " " + customer.getFirstName() + " " + customer.getLastName();
        customerPhone = customer.getPhone();
        setCustomerId(customer.getId());

        customerFio = customer.getFamily() + " " + customer.getFirstName() + " " + customer.getLastName();
        customerPhone = customer.getPhone();
        setTransportId(customer.getId());

        setTransportId(transport.getId());

        if (transport.getParking().isEmpty()) {
            setLatitude(transport.getLatitude());
            setLongitude(transport.getLongitude());
        }
        else {
            ParkingEntity parking = transport.getParking().iterator().next();
            setLatitude(parking.getLatitude());
            setLongitude(parking.getLongitude());
        }

        cost = transport.getCost();

        if (transport.getCustomer().size() > 1) {
            System.out.println("Ox ox error");
        } else {
            CustomerEntity driver = transport.getCustomer().iterator().next();
            driverFio = driver.getFamily() + " " + driver.getFirstName() + " " + driver.getLastName();
            driverPhone = driver.getPhone();
            driverId = driver.getId();
        }


        this.status = "new";
    }

    @Basic
    @Column(name = "customer_fio", nullable = false, insertable = true, updatable = true)
    public String getCustomerFio() {
        return customerFio;
    }

    @Basic
    @Column(name = "customer_phone", nullable = false, insertable = true, updatable = true)
    public String getCustomerPhone() {
        return customerPhone;
    }

    @Basic
    @Column(name = "customer_id", nullable = false, insertable = true, updatable = true)
    public Long getCustomerId() {
        return customerId;
    }

    @Basic
    @Column(name = "transport_id", nullable = false, insertable = true, updatable = true)
    public Long getTransportId() {
        return transportId;
    }

    @Basic
    @Column(name = "driver_fio", nullable = false, insertable = true, updatable = true)
    public String getDriverFio() {
        return driverFio;
    }

    @Basic
    @Column(name = "driver_phone", nullable = false, insertable = true, updatable = true)
    public String getDriverPhone() {
        return driverPhone;
    }

    @Basic
    @Column(name = "driver_id", nullable = true, insertable = true, updatable = true)
    public Long getDriverId() {
        return driverId;
    }

    @Basic
    @Column(name = "latitude", nullable = false, insertable = true, updatable = true)
    public Double getLatitude() {
        return latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = false, insertable = true, updatable = true)
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
    @Column(name = "price", nullable = false, insertable = true, updatable = false)
    public Double getPrice() {
        return price;
    }

    @Basic
    @Column(name = "comment", nullable = false, insertable = true, updatable = false)
    @Type(type="text")
    public String getComment() {
        return comment;
    }

    @Basic
    @Column(name = "status", nullable = false, insertable = true, updatable = false)
    @Type(type="text")
    public String getStatus() {
        return status;
    }
}
