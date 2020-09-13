package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("startAt")
    private Integer startAt;
    @SerializedName("stopAt")
    private Integer stopAt;
    @SerializedName("createdAt")
    private Integer createdAt;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("description")
    private String description;
    @SerializedName("comment")
    private String comment;
    @SerializedName("cost")
    private Double cost;
    @SerializedName("price")
    private Double price;
    @SerializedName("customerFio")
    private String customerFio;
    @SerializedName("customerPhone")
    private String customerPhone;
    @SerializedName("customerId")
    private Long customerId;
    @SerializedName("transportId")
    private Long transportId;
    @SerializedName("driverFio")
    private String driverFio;
    @SerializedName("driverPhone")
    private String driverPhone;
    @SerializedName("driverId")
    private Long driverId;
    @SerializedName("state")
    private String state;
}
