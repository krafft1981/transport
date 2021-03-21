package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Calendar extends AbstractDto {

    @JsonProperty("day_num")
    private Long dayNum;
    @JsonProperty("start_at")
    private Long startAt;
    @JsonProperty("stop_at")
    private Long stopAt;

    public Calendar(Long id, Long dayNum, Date startAt, Date stopAt) {

        setId(id);
        setStartAt(startAt.getTime());
        setStopAt(stopAt.getTime());
        setDayNum(dayNum);
    }

    public Calendar(Long dayNum, Long startAt, Long stopAt) {

        setStartAt(startAt);
        setStopAt(stopAt);
        setDayNum(dayNum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Calendar calendar = (Calendar) o;

        if (!getStartAt().equals(calendar.getStartAt())) return false;
        if (!getStopAt().equals(calendar.getStopAt())) return false;
        return getDayNum().equals(calendar.getDayNum());
    }
}
