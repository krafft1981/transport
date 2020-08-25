package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("driverId")
    private Long driverId;
    @SerializedName("transportId")
    private Long transportId;
    @SerializedName("customerId")
    private Long customerId;
    @SerializedName("startAt")
    private Date startAt;
    @SerializedName("stopAt")
    private Date stopAt;
    @SerializedName("description")
    private String description;
    @SerializedName("comment")
    private String comment;
}
