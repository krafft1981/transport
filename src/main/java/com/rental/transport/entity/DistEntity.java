package com.rental.transport.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "dist")
@Table(
        name = "dist",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "program", name = "dist_program_idx")
        }
)

@Setter
@NoArgsConstructor
public class DistEntity extends AbstractEntity {

    private String program = "";
    private Date createdAt = new Date();

    public DistEntity(String program) {
        setProgram(program);
    }

    @Basic
    @Column(name = "program", nullable = false, insertable = true, updatable = true)
    public String getProgram() {
        return program;
    }

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp with time zone not null default CURRENT_TIMESTAMP")
    public Date getCreatedAt() {
        return createdAt;
    }
}
