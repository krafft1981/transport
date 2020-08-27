package com.rental.transport.service;

import com.rental.transport.entity.AbstractEntity;
import com.rental.transport.dto.AbstractDto;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.security.Principal;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

public interface AbstractInterface<E extends AbstractEntity, D extends AbstractDto> {

    Long count();
    Long count(@NonNull Principal principal);
    void delete(@NonNull Principal principal, @NonNull Long id) throws AccessDeniedException;
    void update(@NonNull Principal principal, @NonNull D dto) throws ObjectNotFoundException, AccessDeniedException;
    List<D> getPage(@NonNull Pageable pageable);
    List<D> getPage(@NonNull Principal principal, @NonNull Pageable pageable);
    D findById(@NonNull Long id) throws ObjectNotFoundException;
    Long create(@NonNull Principal principal);
}
