package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Transport {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("image")
    private String image;
    @SerializedName("capacity")
    private Integer capacity;
    @SerializedName("description")
    private String description;
    @SerializedName("drivers")
    private Set<Long> drivers = new HashSet<>();

    public void addDrivers(Long id) {
        drivers.add(id);
    }
}
