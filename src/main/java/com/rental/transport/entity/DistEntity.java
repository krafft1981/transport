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

import java.util.Objects;

@Entity
@Table(
        name = "dist",
        indexes = {@Index(columnList = "program", name = "dist_program_idx")}
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DistEntity extends AbstractEnabledEntity {

    @Column(columnDefinition = "text")
    private String program;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistEntity that = (DistEntity) o;
        return Objects.equals(this.getProgram(), that.getProgram());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getProgram());
    }
}
