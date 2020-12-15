package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="transport")
@Table(
        name="transport",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportEntity extends AbstractEntity {

    private TypeEntity type;
    private Set<PropertyEntity> property = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();

    public TransportEntity(CustomerEntity customer, TypeEntity type) {

        addCustomer(customer);
        setType(type);
        customer.getParking()
                .stream()
                .forEach(parking -> { addParking(parking); });

        addPropertyList();
    }

    public void addPropertyList() {

        addProperty(new PropertyEntity("Название", "name", ""));
        addProperty(new PropertyEntity("Вместимость", "capacity", "1"));
        addProperty(new PropertyEntity("Описание", "description", ""));
        addProperty(new PropertyEntity("Цена", "price", "0"));
        addProperty(new PropertyEntity("Кворум", "quorum", "1"));
        addProperty(new PropertyEntity("Минимальное время аренды", "minTime", "7200"));
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TypeEntity getType() {
        return type;
    }

    @OneToMany
    public Set<ImageEntity> getImage() {
        return image;
    }

    @ManyToMany
    @JoinTable(name="parking_transport",
            joinColumns=@JoinColumn(name="transport_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="parking_id", nullable = false)
    )
    public Set<ParkingEntity> getParking() {

        return parking;
    }

    @ManyToMany
    @JoinTable(name="customer_transport",
            joinColumns=@JoinColumn(name="transport_id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="customer_id", nullable = false)
    )
    public Set<CustomerEntity> getCustomer() {

        return customer;
    }

    public void addCustomer(CustomerEntity entity) {

        customer.add(entity);
    }

    public void addImage(ImageEntity entity) {

        image.add(entity);
    }

    public void addParking(ParkingEntity entity) {

        if (!getParking().isEmpty()) {
            parking.clear();
        }

        parking.add(entity);
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
