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
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplatesEntity extends AbstractEntity {

    private TypeEntity type;
    private String humanName;
    private String logicName;
    private String value;

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TypeEntity getType() {
        return type;
    }

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
