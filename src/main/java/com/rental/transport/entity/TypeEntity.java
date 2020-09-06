package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name="transportType",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "name", name = "name_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeEntity extends AbstractEntity {

    private String name;

    @Basic
    @Column(name = "name", unique = true, nullable = true, insertable = true, updatable = true)
    public String getName() {
        return name;
    }
}
