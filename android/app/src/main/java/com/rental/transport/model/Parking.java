package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Parking {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("description")
    private String description;
    @SerializedName("customer")
    private Set<Long> customer = new HashSet<>();
    @SerializedName("transport")
    private Set<Long> transport = new HashSet<>();

    public void addCustomer(Long id) {
        customer.add(id);
    }

    public void addTransport(Long id) {
        transport.add(id);
    }
}
