package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "transport",
        indexes = {
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx"),
                @Index(columnList = "enable", name = "transport_enable_idx")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TransportEntity extends AbstractEnabledEntity {

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id", nullable = false)
    private TransportTypeEntity type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "transport_image",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "image_id", nullable = false)
    )
    private Set<ImageEntity> image = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false)
    )
    private Set<CustomerEntity> customers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parking_transport",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "parking_id", nullable = false)
    )
    private Set<ParkingEntity> parking = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "transport_property",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "property_id", nullable = false)
    )
    private Set<PropertyEntity> properties = new HashSet<>();

    public TransportEntity addImage(ImageEntity entity) {
        image.add(entity);
        return this;
    }

    public TransportEntity delImage(ImageEntity entity) {
        image.remove(entity);
        return this;
    }

    public TransportEntity addCustomer(CustomerEntity entity) {
        customers.add(entity);
        return this;
    }

    public TransportEntity delCustomer(CustomerEntity entity) {
        customers.remove(entity);
        return this;
    }

    public TransportEntity isEnable(boolean isEnable) {
        this.setEnable(isEnable);
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
