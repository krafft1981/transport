package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

public class Calendar {

    @SerializedName("id")
    private Long id;
    @SerializedName("customerId")
    private Long customerId;
    @SerializedName("startAt")
    private Integer startAt;
    @SerializedName("stopAt")
    private Integer stopAt;
    @SerializedName("orderId")
    private Long orderId;
}
