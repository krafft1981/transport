package com.rental.transport.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
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
public class MessageEntity extends AbstractEntity {

    private String text = "";
    private CustomerEntity customer;
    private Date date;

    public MessageEntity(CustomerEntity customer, String text) {
        setCustomer(customer);
        setText(text);
        setDate(currentTime());
    }

    public MessageEntity() {
        setDate(currentTime());
    }

    private Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());
        return calendar.getTime();
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
