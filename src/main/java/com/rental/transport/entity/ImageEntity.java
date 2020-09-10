package com.rental.transport.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(
        name="image",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity extends AbstractEntity {

    private String data = "";

    @Basic
    @Column(name = "data", nullable = false, insertable = true, updatable = true)
    @Type(type="text")
    public String getData() {
        return data;
    }
}
