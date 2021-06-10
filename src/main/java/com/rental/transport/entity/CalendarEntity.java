package com.rental.transport.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private Set<MessageEntity> message = new HashSet();

    public CalendarEntity(Long day, Integer[] hours) {
        setDay(day);
        setHours(hours);
    }

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

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "calendar_message",
            joinColumns = @JoinColumn(name = "calendar_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "message_id", nullable = false)
    )
    public Set<MessageEntity> getMessage() {
        return message;
    }
}
