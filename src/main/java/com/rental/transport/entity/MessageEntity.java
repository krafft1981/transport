package com.rental.transport.entity;

import java.util.Date;
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

@Entity
@Table(
        name = "message",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "message_customer_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity extends AbstractEntity {

    private String text = "";
    private CustomerEntity customer;
    private Date date = new Date();

    public MessageEntity(CustomerEntity customer, String text) {
        setCustomer(customer);
        setText(text);
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @Column(name = "text", nullable = false, insertable = true, updatable = true)
    @Type(type = "text")
    public String getText() {
        return text;
    }

    @Basic
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public Date getDate() {
        return date;
    }
}
