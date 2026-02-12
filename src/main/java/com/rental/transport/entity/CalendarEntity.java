package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import com.rental.transport.enums.CalendarTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(
        name = "calendar",
        indexes = {
                @Index(columnList = "day", name = "calendar_day_num_id_idx"),
                @Index(columnList = "type", name = "calendar_type_idx")
        }
)

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CalendarEntity extends AbstractEnabledEntity {

    @Column(nullable = false)
    private Long day;

    private Integer[] hours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private CalendarTypeEnum type = CalendarTypeEnum.NOTE;

//    @Column(nullable = false)
    private UUID objectId;

    private UUID orderId;

    @OneToOne(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER
    )
    @JoinColumn(nullable = false)
    private MessageEntity message;

    public CalendarEntity(Long day, Integer[] hours, MessageEntity message) {
        setDay(day);
        setHours(hours);
        setMessage(message);
    }
}
