package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Calendar extends AbstractDto {

    @JsonProperty("day_num")
    private Long dayNum;
    @JsonProperty("start_at")
    private Long startAt;
    @JsonProperty("stop_at")
    private Long stopAt;

    public Calendar(Long dayNum, Date startAt, Date stopAt) {

        setDayNum(dayNum);
        setStartAt(startAt.getTime());
        setStopAt(stopAt.getTime());
    }

    public Calendar(Long dayNum, Long startAt, Long stopAt) {

        setDayNum(dayNum);
        setStartAt(startAt);
        setStopAt(stopAt);
    }

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
