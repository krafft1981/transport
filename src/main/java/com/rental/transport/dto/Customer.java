package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends AbstractIdDto {

    @JsonProperty("account")
    private String account;
    @JsonProperty("send_email")
    private Boolean sendEmail;
    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();
    @JsonProperty("image")
    private Set<Long> image = new HashSet<>();
    @JsonProperty("transport")
    private Set<Long> transport = new HashSet<>();
    @JsonProperty("parking")
    private Set<Long> parking = new HashSet<>();

    public void addImage(Long value) {
        image.add(value);
    }
    public void addTransport(Long value) {
        transport.add(value);
    }
    public void addParking(Long value) {
        parking.add(value);
    }
}
