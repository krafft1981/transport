package com.rental.transport.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "calendar")
@Table(
        name="calendar",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "customer_id", name = "customer_id_idx" )
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Long customerId;
    private Date startAt;
    private Date stopAt;

    @Basic
    @Column(name = "customer_id", unique = false, nullable = false, insertable = true, updatable = false)
    public Long getCustomerId() {
        return customerId;
    }

    @Basic
    @Column(name = "start_at", nullable = true, columnDefinition = "timestamp with time zone not null")
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", nullable = true, columnDefinition = "timestamp with time zone not null")
    public Date getStopAt() {
        return stopAt;
    }
}
