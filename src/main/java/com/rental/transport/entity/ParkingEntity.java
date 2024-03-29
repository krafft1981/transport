package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
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
        catalog = "relationship",
        indexes = {
                @Index(columnList = "enable", name = "parking_enable_idx")
        }
)

@Setter
@NoArgsConstructor
public class ParkingEntity extends AbstractEnabledEntity {

    private Set<PropertyEntity> property = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();
    private Set<TransportEntity> transport = new HashSet<>();

    public ParkingEntity(CustomerEntity entity) {
        addCustomer(entity);
    }

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "parking_property",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "property_id", nullable = false)
    )
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

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "parking_image",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "image_id", nullable = false)
    )
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

    public void delImage(ImageEntity entity) {
        image.remove(entity);
    }

    public void addProperty(PropertyEntity entity) {

        String name = entity.getType().getLogicName();
        property.add(
                property.stream()
                        .filter(propertyEntity -> propertyEntity.getType().getLogicName().equals(name))
                        .findFirst()
                        .orElse(entity)
        );
    }

    public void addProperty(PropertyEntity... entryes) {

        for (PropertyEntity property : entryes)
            addProperty(property);
    }
}
