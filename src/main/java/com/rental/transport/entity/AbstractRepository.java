package com.rental.transport.entity;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AbstractRepository <E extends AbstractEntity> extends PagingAndSortingRepository<E, Long> {

}
