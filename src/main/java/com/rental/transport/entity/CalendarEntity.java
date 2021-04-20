package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "calendar")
@Table(
        name = "calendar",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "day_num", name = "calendar_day_num_id_idx")
        }
)

@Setter
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Long dayNum;
    private Integer hour;

    public CalendarEntity(Long dayNum, Integer hour) {
        setHour(hour);
        setDayNum(dayNum);
    }

    @Basic
    @Column(name = "hour", nullable = false, insertable = true, updatable = true)
    public Integer getHour() {
        return hour;
    }

    @Basic
    @Column(name = "day_num", nullable = false, insertable = true, updatable = true)
    public Long getDayNum() {
        return dayNum;
    }
}
