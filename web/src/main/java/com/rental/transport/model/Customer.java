package com.rental.transport.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
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
    @SerializedName("startWorkAt")
    private Integer startWorkAt;
    @SerializedName("stopWorkAt")
    private Integer stopWorkAt;
    @SerializedName("workAtWeekEnd")
    private Boolean workAtWeekEnd;
    @SerializedName("images")
    private List<Image> images;
    @SerializedName("transports")
    private List<Long> transports;
    @SerializedName("parkings")
    private List<Long> parkings;
}
