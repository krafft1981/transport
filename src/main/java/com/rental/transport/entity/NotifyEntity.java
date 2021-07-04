package com.rental.transport.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
import org.hibernate.annotations.TypeDef;

@Entity
@Table(
        name = "notify",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "notify_customer_id_idx")
        }
)
@TypeDef(
        typeClass = JsonBinaryType.class,
        defaultForType = JsonNode.class
)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEntity extends AbstractEntity {

    private CustomerEntity customer;
    private Date date = currentTime();
    private String message;
    private String action;

    public NotifyEntity(CustomerEntity customer, String action, String message) {
        setCustomer(customer);
        setAction(action);
        setMessage(message);
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getDate() {
        return date;
    }

    @Basic
    @Column(name = "message", nullable = false, insertable = true, updatable = true)
    public String getMessage() {
        return message;
    }

    @Basic
    @Column(name = "action", nullable = false, insertable = true, updatable = true)
    public String getAction() {
        return action;
    }
}
