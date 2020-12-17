package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name="confirmation",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "customer_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationEntity extends AbstractEntity {

    private CustomerEntity customer;
    private OrderEntity order;

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    public OrderEntity getOrder() {
        return order;
    }
}
