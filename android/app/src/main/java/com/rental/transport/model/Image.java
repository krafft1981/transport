package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Image {

    @SerializedName("id")
    private Long id;
    @SerializedName("data")
    private String data;
}