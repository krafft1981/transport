package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class OrderEntity extends AbstractEntity {

    @OneToOne(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(nullable = false)
    private RequestEntity request;

    public OrderEntity(RequestEntity request) {
        request.setOrder(this);
        this.request = request;
    }
}
