package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity(name = "calendar")
@Table(
        name = "calendar",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "day", name = "calendar_day_num_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Long day;
    private Integer[] hours;
    private String message = "";

    public CalendarEntity(Long day, Integer[] hours) {
        setDay(day);
        setHours(hours);
    }

    @Type(type = "int-array")
    @Column(
            name = "hours",
            columnDefinition = "integer[]"
    )
    public Integer[] getHours() {
        return hours;
    }

    @Basic
    @Column(name = "day", nullable = false, insertable = true, updatable = true)
    public Long getDay() {
        return day;
    }

    @Basic
    @Column(name = "message", nullable = false, insertable = true, updatable = true)
    @Type(type = "text")
    public String getMessage() {
        return message;
    }
}
