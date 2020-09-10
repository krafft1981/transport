package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import lombok.Data;

@Data
public class WorkTime {

    @SerializedName("id")
    private Long id;
    @SerializedName("accountId")
    private Long accountId;
    @SerializedName("startAt")
    private Date startAt;
    @SerializedName("stopAt")
    private Date stopAt;
    @SerializedName("workAtWeekEnd")
    private Boolean workAtWeekEnd;
}
