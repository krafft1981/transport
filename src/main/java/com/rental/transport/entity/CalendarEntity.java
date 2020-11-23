package com.rental.transport.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
                @Index(columnList = "day_num", name = "day_num_id_idx" ),
                @Index(columnList = "order_id", name = "order_id_idx" )
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Date startAt;
    private Date stopAt;
    private Long dayNum;
    private OrderEntity order;
    private CustomerEntity customer;

    @Basic
    @Column(name = "start_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    public Date getStartAt() {
        return startAt;
    }

    @Basic
    @Column(name = "stop_at", nullable = false, columnDefinition = "timestamp with time zone not null")
    public Date getStopAt() {
        return stopAt;
    }

    @Basic
    @Column(name = "day_num", nullable = false, insertable = true, updatable = true)
    public Long getDayNum() {
        return dayNum;
    }

    @Column(name = "order_id", nullable = true, insertable = true, updatable = true)
    public OrderEntity getOrder() {
        return order;
    }

    @ManyToOne
    public CustomerEntity getCustomer() {
        return customer;
    }
}
