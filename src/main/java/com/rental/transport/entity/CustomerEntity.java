package com.rental.transport.entity;

import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
    private Set<ImageEntity> image = new HashSet<>();
    private Set<TransportEntity> transport = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<PropertyEntity> property = new HashSet<>();

    public CustomerEntity(String account) {
        setAccount(account);
        addPropertyList();
    }

    public void addPropertyList() {
        addProperty(new PropertyEntity("ФИО", "fio", ""));
        addProperty(new PropertyEntity("Телефон", "phone", ""));
        addProperty(new PropertyEntity("Время начала работы", "startWorkTime", "8"));
        addProperty(new PropertyEntity("Время окончания работы", "stopWorkTime", "18"));
        addProperty(new PropertyEntity("Работает в субб./воскр.", "workAtWeekEnd", "1"));
        addProperty(new PropertyEntity("Описание", "description", ""));
    }

    @Basic
    @Column(name = "account", unique = true, nullable = false, insertable = true, updatable = false)
    @NotEmpty
    public String getAccount() {
        return account;
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

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    public void addTransport(TransportEntity entity) {
        transport.add(entity);
    }

    public void addParking(ParkingEntity entity) {
        parking.add(entity);
    }

    public void addImage(ImageEntity entity) {
        image.add(entity);
    }

    public void addProperty(PropertyEntity entity) {

        String name = entity.getLogicName();

        entity = property.stream()
                .filter(propertyEntity -> propertyEntity.getLogicName().equals(name))
                .findFirst()
                .orElse(entity);

        property.add(entity);
    }
}
