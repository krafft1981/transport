package com.rental.transport.entity;

import com.rental.transport.enums.PropertyTypeEnum;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "property_type")
@Table(
        name = "property_type",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "logic_name", name = "logic_name_idx")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"logic_name"})
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTypeEntity extends AbstractEntity {

    private String logicName = "";
    private String humanName = "";
    private PropertyTypeEnum type = PropertyTypeEnum.String;

    @Basic
    @Column(name = "logic_name", unique = true, nullable = false, insertable = true, updatable = true)
    public String getLogicName() {
        return logicName;
    }

    @Basic
    @Column(name = "human_name", nullable = false, insertable = true, updatable = true)
    public String getHumanName() {
        return humanName;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, insertable = true, updatable = true)
    public PropertyTypeEnum getType() {
        return type;
    }
}
