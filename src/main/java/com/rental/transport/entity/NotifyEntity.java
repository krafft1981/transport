package com.rental.transport.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(
        name = "notify",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "notify_customer_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEntity extends AbstractEntity {

    private String text;
    private CustomerEntity customer;
    private Date date = currentTime();

    public NotifyEntity(CustomerEntity customer, String text) {
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getDate() {
        return date;
    }
}
