package com.rental.transport.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tariff")
@Table(
        name = "tariff",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TariffEntity extends AbstractEntity {

    private Double cost;
}
