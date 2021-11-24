package com.rental.transport.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(
        name = "image",
        schema = "public",
        catalog = "relationship"
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity extends AbstractEntity {

    private byte[] data;

    @Basic
    @Column(name = "data", nullable = false, insertable = true, updatable = true)
    public byte[] getData() {
        return data;
    }
}
