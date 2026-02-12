package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "customer",
        indexes = {
                @Index(columnList = "enable", name = "customer_account_enabled_idx")
        }
)
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends AbstractEnabledEntity {

    @Column(nullable = false)
    private String timeZone = "Europe/Samara";

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_image",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "image_id", nullable = false)
    )
    private Set<ImageEntity> image = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "transport_id", nullable = false))
    private Set<TransportEntity> transport = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_parking",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "parking_id", nullable = false)
    )
    private Set<ParkingEntity> parking = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "customer_property",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "property_id", nullable = false)
    )
    private Set<PropertyEntity> properties = new HashSet<>();


    public CustomerEntity addImage(ImageEntity entity) {
        image.add(entity);
        return this;
    }

    public CustomerEntity delImage(ImageEntity entity) {
        image.remove(entity);
        return this;
    }

    public CustomerEntity isEnable(boolean isEnable) {
        this.setEnable(isEnable);
        return this;
    }

    public CustomerEntity addProperty(PropertyEntity entity) {
        properties.add(entity);
        return this;
    }

    public CustomerEntity addProperty(final PropertyEntity... entryes) {
        for (final var property : entryes)
            addProperty(property);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
