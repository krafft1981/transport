package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("cost")
    private Double cost;
    @SerializedName("minHour")
    private Integer minHour;
    @SerializedName("quorum")
    private Integer quorum;
    @SerializedName("parking")
    private List<Long> parking = new ArrayList<>();
    @SerializedName("customers")
    private List<Long> customers = new ArrayList<>();
    @SerializedName("images")
    private List<Long> images = new ArrayList<>();
}
