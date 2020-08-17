package com.rental.transport;

import lombok.Data;

@Data
public class ListItem {

    private String countryName;
    private String flagName;
    private int capacity;

    public ListItem(String countryName, String flagName, int capacity) {
        this.countryName= countryName;
        this.flagName= flagName;
        this.capacity = capacity;
    }

    @Override
    public String toString()  {
        return this.countryName + " (Стоимость: " + this.capacity + ")";
    }
}
