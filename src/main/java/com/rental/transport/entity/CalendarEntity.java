package com.rental.transport.entity;

import java.util.Date;
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
                @Index(columnList = "day_num", name = "day_num_id_idx")
        }
)

@Setter
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Date startAt;
    private Date stopAt;
    private Long dayNum;

    public CalendarEntity(Long dayNum, Long startAt, Long stopAt) {
        setStartAt(new Date(startAt));
        setStopAt(new Date(stopAt));
        setDayNum(dayNum);
    }

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CalendarEntity{");
        sb.append("startAt=").append(startAt);
        sb.append(", stopAt=").append(stopAt);
        sb.append(", dayNum=").append(dayNum);
        sb.append('}');
        return sb.toString();
    }
}
