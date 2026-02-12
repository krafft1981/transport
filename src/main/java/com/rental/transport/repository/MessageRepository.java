package com.rental.transport.repository;

import com.rental.transport.entity.MessageEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends IRepository<MessageEntity> {

}
