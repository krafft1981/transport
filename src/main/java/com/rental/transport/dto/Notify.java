package com.rental.transport.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notify {
    private Request request;
    private List<Event> events;
    private String action;
}
