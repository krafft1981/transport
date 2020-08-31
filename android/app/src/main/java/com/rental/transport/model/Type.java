package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Type {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;

    @Override
    public String toString() {
        return name;
    }
}
