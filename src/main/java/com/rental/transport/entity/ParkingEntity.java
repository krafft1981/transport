package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "parking",
        schema = "public",
        catalog = "relationship"
)

@Setter
@NoArgsConstructor
public class ParkingEntity extends AbstractEntity {

    private Set<PropertyEntity> property = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();
    private Set<TransportEntity> transport = new HashSet<>();

    public ParkingEntity(CustomerEntity entity) {
        addCustomer(entity);
        addPropertyList();
    }

    public void addPropertyList() {
        addProperty(new PropertyEntity("Название", "name", "Название не указано", "String"));
        addProperty(new PropertyEntity("Широта", "latitude", "0", "Double"));
        addProperty(new PropertyEntity("Долгота", "longitude", "0", "Double"));
        addProperty(new PropertyEntity("Адрес", "address", "Адрес не указан", "String"));
        addProperty(new PropertyEntity("Ближайший населённый пункт", "locality", "Тольятти", "String"));
        addProperty(new PropertyEntity("Район", "region", "Регион", "String"));
        addProperty(new PropertyEntity("Описание", "description", "Чертовски клёвое место", "String"));
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    @ManyToMany
    @JoinTable(name = "customer_parking",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false)
    )
    public Set<CustomerEntity> getCustomer() {
        return customer;
    }

    @ManyToMany
    @JoinTable(name = "parking_transport",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "transport_id", nullable = false)
    )
    public Set<TransportEntity> getTransport() {
        return transport;
    }

    @OneToMany
    public Set<ImageEntity> getImage() {
        return image;
    }

    public void addTransport(TransportEntity entity) {
        transport.add(entity);
    }

    public void addCustomer(CustomerEntity entity) {
        customer.add(entity);
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
