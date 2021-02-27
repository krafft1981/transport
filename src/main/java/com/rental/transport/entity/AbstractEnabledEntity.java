package com.rental.transport.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Setter;

@Setter
@MappedSuperclass
public class AbstractEnabledEntity extends AbstractEntity {

    private Boolean enable = true;

    @Column(name = "enable", unique = false, updatable = true, nullable = false, columnDefinition = "boolean default true")
    public Boolean getEnable() {
        return enable;
    }
}
