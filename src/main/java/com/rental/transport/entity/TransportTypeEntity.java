package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "transport_type")
@Table(
        name = "transport_type",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "name", name = "transport_type_name_idx"),
                @Index(columnList = "enable", name = "transport_type_enabled_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportTypeEntity extends AbstractEnabledEntity {

    private String name;

    @Basic
    @Column(name = "name", unique = true, nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }
}
