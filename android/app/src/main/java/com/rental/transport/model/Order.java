package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Order {

    @SerializedName("id")
    private Long id;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("customerPhone")
    private String customerPhone;
    @SerializedName("customer")
    private Long customer;
    @SerializedName("transport")
    private Transport transport;
    @SerializedName("driver")
    private List<Customer> driver = new ArrayList<>();
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("startAt")
    private Integer startAt;
    @SerializedName("stopAt")
    private Integer stopAt;
    @SerializedName("createdAt")
    private Integer createdAt;
    @SerializedName("cost")
    private Double cost;
    @SerializedName("price")
    private Double price;
    @SerializedName("comment")
    private String comment;
    @SerializedName("state")
    private String state;
}
