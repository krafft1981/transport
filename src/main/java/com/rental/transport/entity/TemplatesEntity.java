package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "templates")
@Table(
        name = "templates",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx"),
                @Index(columnList = "property_type_id", name = "property_type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplatesEntity extends AbstractEntity {

    private TypeEntity transportType;
    private PropertyTypeEntity propertyTypeEntity;
    private String value = "";

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TypeEntity getTransportType() {
        return transportType;
    }

    @ManyToOne
    @JoinColumn(name = "property_type_id", nullable = false, referencedColumnName = "id")
    public PropertyTypeEntity getPropertyTypeEntity() {
        return propertyTypeEntity;
    }

    @Basic
    @Column(name = "value", nullable = false, insertable = true, updatable = true)
    public String getValue() {
        return value;
    }
}
