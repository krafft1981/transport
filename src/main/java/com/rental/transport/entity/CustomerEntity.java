package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name="customer",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "account", name = "account_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends AbstractEntity {

    private String account;
    private String firstName = "";
    private String lastName = "";
    private String family = "";
    private String phone = "";
    private Integer startWorkAt;
    private Integer stopWorkAt;
    private Boolean workAtWeekEnd = false;
    private Set<ImageEntity> image = new HashSet<>();
    private Set<TransportEntity> transport = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();

    public CustomerEntity(String account) {
        setAccount(account);
    }

    @Basic
    @Column(name = "account", unique = true, nullable = false, insertable = true, updatable = false)
    @NotEmpty
    public String getAccount() {
        return account;
    }
    
    @Basic
    @Column(name = "firstName", nullable = false, insertable = true, updatable = true)
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true)
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "family", nullable = false, insertable = true, updatable = true)
    public String getFamily() {
        return family;
    }

    @Basic
    @Column(name = "phone", nullable = false, insertable = true, updatable = true)
    public String getPhone() {
        return phone;
    }

    @Basic
    @Column(name = "start_work_at", nullable = true, insertable = true, updatable = true)
    public Integer getStartWorkAt() {
        return startWorkAt;
    }

    @Basic
    @Column(name = "stop_work_at", nullable = true, insertable = true, updatable = true)
    public Integer getStopWorkAt() {
        return stopWorkAt;
    }

    @Basic
    @Column(name = "work_at_week_end", nullable = false, insertable = true, updatable = true)
    public Boolean getWorkAtWeekEnd() {
        return workAtWeekEnd;
    }

    @OneToMany
    public Set<ImageEntity> getImage() {
        return image;
    }

    @ManyToMany
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "transport_id", nullable = false))
    public Set<TransportEntity> getTransport() {
        return transport;
    }

    @ManyToMany
    @JoinTable(name="customer_parking",
            joinColumns=@JoinColumn(name="customer_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="parking_id", nullable = false)
    )
    public Set<ParkingEntity> getParking() {
        return parking;
    }

    public void addTransport(TransportEntity entity) {
        transport.add(entity);
    }

    public void addParking(ParkingEntity entity) {

        if (getParking().isEmpty()) {
            parking.add(entity);
        }
    }

    public void addImage(ImageEntity entity) {
        image.add(entity);
    }
}
