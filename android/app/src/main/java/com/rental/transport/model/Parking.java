package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Parking {

    @SerializedName("id")
    private Long id;
    @SerializedName("address")
    private String address;
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

    @Override
    public String toString() {
        return "Parking{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", customer=" + customer +
                ", transport=" + transport +
                '}';
    }
}
