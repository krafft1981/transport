package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
@Table(name = "image")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ImageEntity extends AbstractEntity {

    @JdbcTypeCode(Types.BINARY)
    private byte[] body;
}
