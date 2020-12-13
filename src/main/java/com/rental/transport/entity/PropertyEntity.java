package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="property")
@Table(
        name="property",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyEntity extends AbstractEntity {

    private String humanName = "";
    private String logicName = "";
    private String value = "";

    @Basic
    @Column(name = "human_name", nullable = false, insertable = true, updatable = true)
    public String getHumanName() {
        return humanName;
    }

    @Basic
    @Column(name = "logic_name", nullable = false, insertable = true, updatable = true)
    public String getLogicName() {
        return logicName;
    }

    @Basic
    @Column(name = "value", nullable = false, insertable = true, updatable = true)
    public String getValue() {
        return value;
    }
}
