package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Transport {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("capacity")
    private Integer capacity;
    @SerializedName("description")
    private String description;
    @SerializedName("cost")
    private String cost;
    @SerializedName("parking")
    private Long parking;
    @SerializedName("customers")
    private List<Long> customers;
    @SerializedName("images")
    private List<Long> images;
}
