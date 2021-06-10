package com.rental.transport.entity;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(
        name = "notebook",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "notebook_customer_id_idx"),
                @Index(columnList = "calendar_id", name = "notebook_calendar_id_idx")
        }
)

@Setter
@AllArgsConstructor
public class NoteBookEntity extends AbstractEntity {

    private CalendarEntity calendar;
    private CustomerEntity customer;
    private Date date = currentTime();
    private String text = "";

    private Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(new Date());
        return calendar.getTime();
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "calendar_id", referencedColumnName = "id")
    public CalendarEntity getCalendar() {
        return calendar;
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
