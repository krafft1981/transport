package com.rental.transport.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity(name = "review")
@Table(
    name = "review",
    schema = "public",
    catalog = "relationship",
    indexes = {
        @Index(columnList = "customer_id", name = "review_customer_id_idx"),
        @Index(columnList = "transport_id", name = "review_transport_id_idx")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "customer_id",
            "transport_id"
        })
    }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity extends AbstractEntity {

    CustomerEntity customer;
    TransportEntity transport;

    @Min(1)
    @Max(5)
    Long score;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @ManyToOne
    @JoinColumn(name = "transport_id", referencedColumnName = "id")
    public TransportEntity getTransport() {
        return transport;
    }

    @Basic
    @Column(name = "score", nullable = false, updatable = false)
    public Long getScore() {
        return score;
    }
}
