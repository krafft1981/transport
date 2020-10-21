package com.rental.transport.entity;

import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name="event",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity extends AbstractEntity {

    private Set<CalendarEntity> calendar;
    private CustomerEntity customer;
    private String eventType;
    private Long eventIndex;

    @OneToMany
    public Set<CalendarEntity> getCalendar() {
        return calendar;
    }

    @Basic
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    public CustomerEntity getCustomer() {
        return customer;
    }

    @Basic
    @Column(name = "event_type", nullable = false, insertable = true, updatable = true)
    public String getEventType() {
        return eventType;
    }

    @Basic
    @Column(name = "event_index", nullable = false, insertable = true, updatable = true)
    public Long getEventIndex() {
        return eventIndex;
    }
}
