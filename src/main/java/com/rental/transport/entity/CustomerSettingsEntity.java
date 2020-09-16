package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity(name="customer_settings")
@Table(
        name="customer_settings",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "customer_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSettingsEntity extends AbstractEntity {

    private CustomerEntity customer;
    private String name = "";
    private String value = "";

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "value", nullable = false, insertable = true, updatable = true)
    @Type(type="text")
    public String getValue() {
        return value;
    }
}
