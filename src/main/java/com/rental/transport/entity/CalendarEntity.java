package com.rental.transport.entity;

import com.rental.transport.enums.CalendarTypeEnum;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
                @Index(columnList = "day", name = "calendar_day_num_id_idx"),
                @Index(columnList = "type", name = "calendar_type_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEntity extends AbstractEntity {

    private Long day;
    private Integer[] hours;
    private CalendarTypeEnum type;
    private Long objectId;
    private Long orderId;
    private String note = "";

    @Basic
    @Column(name = "day", nullable = false, insertable = true, updatable = true)
    public Long getDay() {
        return day;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, insertable = true, updatable = true)
    public CalendarTypeEnum getType() {
        return type;
    }

    @Basic
    @Column(name = "object_id", nullable = false, insertable = true, updatable = true)
    public Long getObjectId() {
        return objectId;
    }

    @Basic
    @Column(name = "note", nullable = false, insertable = true, updatable = true)
    public String getNote() {
        return note;
    }
}
