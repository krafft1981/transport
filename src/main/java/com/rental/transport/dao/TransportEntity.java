package com.rental.transport.dao;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
                @Index(columnList = "type", name = "type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportEntity extends AbstractEntity  {

    private String name;
    private String type;
    private Blob image;
    private Integer capacity;
    private String description;
    private Set<CustomerEntity> customer = new HashSet<>();

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "type", nullable = true, insertable = true, updatable = true)
    public String getType() {
        return type;
    }

    @Basic
    @Column(name = "image", nullable = true, insertable = true, updatable = true)
    public Blob getImage() {
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
}
