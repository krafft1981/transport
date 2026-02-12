package com.rental.transport.repository.template;

import com.rental.transport.entity.template.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface IRepository<E extends AbstractEntity> extends JpaRepository<E, UUID> {

}
