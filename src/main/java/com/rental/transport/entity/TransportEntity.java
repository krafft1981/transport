package com.rental.transport.entity;

import java.util.Currency;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
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
public class TransportEntity extends AbstractEntity  {

    private String name;
    private TypeEntity type;
    private Integer capacity;
    private String description;
    private Currency cost;
    private ParkingEntity parking;
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    @Basic
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TypeEntity getType() {
        return type;
    }

    @OneToMany
    public Set<ImageEntity> getImage() {
        return image;
    }

    @Basic
    @Column(name = "capacity", nullable = true, insertable = true, updatable = true)
    public Integer getCapacity() {
        return capacity;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true)
    @Type(type="text")
    public String getDescription() {
        return description;
    }

    @Basic
    @Column(name = "cost", nullable = true, insertable = true, updatable = false)
    public Currency getCost() {
        return cost;
    }

    @ManyToOne
    public ParkingEntity getParking() {
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
}
