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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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
public class TransportEntity extends AbstractEntity  {

    private String name = "";
    private TypeEntity type;
    private Integer capacity = 1;
    private String description = "";
    private Double cost = 0.0;
    private Integer minHour = 1;
    private Integer quorum = 1;
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();

    public TransportEntity(CustomerEntity customer, TypeEntity type) {

        addCustomer(customer);
        setType(type);
        customer.getParking()
                .stream()
                .forEach(parking -> { addParking(parking); });
    }
    
    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
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

    @Basic
    @Column(name = "capacity", nullable = false, insertable = true, updatable = true)
    public Integer getCapacity() {
        return capacity;
    }

    @Basic
    @Column(name = "description", nullable = false, insertable = true, updatable = true)
    @Type(type="text")
    public String getDescription() {
        return description;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = false)
    public Double getCost() {
        return cost;
    }

    @Basic
    @Column(name = "min_hour", nullable = false, insertable = true, updatable = true)
    public Integer getMinHour() {
        return minHour;
    }

    @Basic
    @Column(name = "quorum", nullable = false, insertable = true, updatable = true)
    public Integer getQuorum() {
        return quorum;
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
}
