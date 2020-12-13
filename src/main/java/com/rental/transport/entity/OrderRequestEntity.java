package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "orders_request")
@Table(
        name="orders_request",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestEntity extends AbstractEntity {

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
