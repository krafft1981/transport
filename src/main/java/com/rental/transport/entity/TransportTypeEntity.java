package com.rental.transport.entity;

import com.rental.transport.entity.template.AbstractEnabledEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(
        name = "transport_type",
        indexes = {
                @Index(columnList = "name", name = "transport_type_name_idx"),
                @Index(columnList = "enable", name = "transport_type_enabled_idx")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TransportTypeEntity extends AbstractEnabledEntity {

    @Column(unique = true, nullable = false)
    private String name;

    public TransportTypeEntity isEnable(boolean isEnable) {
        this.setEnable(isEnable);
        return this;
    }
}
