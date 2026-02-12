package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Calendar;

@Entity
@Table(name = "message")
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class MessageEntity extends AbstractEnabledEntity {

    @Column(length = 128, nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    public MessageEntity(CustomerEntity customer, String text) {
        setCustomer(customer);
        setText(text);
    }
}
