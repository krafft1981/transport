package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Customer {

    @SerializedName("id")
    private Long id;
    @SerializedName("account")
    private String account;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("family")
    private String family;
    @SerializedName("phone")
    private String phone;
    @SerializedName("image")
    private Blob image;

    @SerializedName("id")
    private Set<Long> transport = new HashSet<>();

    public void addTransport(Long id) {
        transport.add(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", family='" + family + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + image +
                ", transport=" + transport +
                '}';
    }
}
