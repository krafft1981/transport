package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "property_type")
@Table(
        name = "property_type",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypeEntity extends AbstractEntity {

    private String humanName = "";
    private String logicName = "";
    private String type = "String";

    @Basic
    @Column(name = "human_name", nullable = false, insertable = true, updatable = true)
    public String getHumanName() {
        return humanName;
    }

    @Basic
    @Column(name = "logic_name", unique = true, nullable = false, insertable = true, updatable = true)
    public String getLogicName() {
        return logicName;
    }

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true)
    public String getType() {
        return type;
    }
}
