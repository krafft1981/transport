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

@Entity(name = "search")
@Table(
        name = "search",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchEntity extends AbstractEntity {

    private TypeEntity type;
    private String key;

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TypeEntity getType() {
        return type;
    }

    @Basic
    @Column(name = "key", nullable = false, insertable = true, updatable = true)
    public String getKey() {
        return key;
    }
}
