package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "property")
@Table(
        name = "property",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "property_type_id", name = "property_type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyEntity extends AbstractEntity {

    private PropertyTypeEntity type;
    private String value = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_type_id", referencedColumnName = "id")
    public PropertyTypeEntity getType() {
        return type;
    }

    @Basic
    @Column(name = "value", nullable = false, insertable = true, updatable = true)
    public String getValue() {
        return value;
    }
}
