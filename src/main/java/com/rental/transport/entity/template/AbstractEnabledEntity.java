package com.rental.transport.entity.template;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@MappedSuperclass
@Accessors(chain = true)
@Setter
@Getter
public class AbstractEnabledEntity extends AbstractEntity {

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean enable = true;
}
