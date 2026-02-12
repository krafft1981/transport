package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "parking",
        indexes = {
                @Index(columnList = "enable", name = "parking_enable_idx")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ParkingEntity extends AbstractEnabledEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_parking",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false)
    )
    private Set<CustomerEntity> customer = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parking_transport",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "transport_id", nullable = false)
    )
    private Set<TransportEntity> transport = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parking_image",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "image_id", nullable = false)
    )
    private Set<ImageEntity> image = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "parking_property",
            joinColumns = @JoinColumn(name = "parking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "property_id", nullable = false)
    )
    private Set<PropertyEntity> properties = new HashSet<>();

    public ParkingEntity isEnable(boolean isEnable) {
        this.setEnable(isEnable);
        return this;
    }

    public ParkingEntity addDriver(CustomerEntity entity) {
        customer.add(entity);
        return this;
    }

    public ParkingEntity delDriver(CustomerEntity entity) {
        customer.remove(entity);
        return this;
    }

    public ParkingEntity addImage(ImageEntity entity) {
        image.add(entity);
        return this;
    }

    public ParkingEntity delImage(ImageEntity entity) {
        image.remove(entity);
        return this;
    }

    public void addProperty(PropertyEntity entity) {
        properties.add(entity);
    }

    public void addProperty(PropertyEntity... entryes) {

        for (PropertyEntity property : entryes)
            addProperty(property);
    }
}
