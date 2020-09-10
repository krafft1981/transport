package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("driver")
    private Customer driverId;
    @SerializedName("transport")
    private Transport transport;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("parking")
    private Parking parking;
    @SerializedName("startAt")
    private Date startAt;
    @SerializedName("stopAt")
    private Date stopAt;
    @SerializedName("createdAt")
    private Date createdAt;
    @SerializedName("description")
    private String description;
    @SerializedName("comment")
    private String comment;
    @SerializedName("price")
    private Double price;
}
