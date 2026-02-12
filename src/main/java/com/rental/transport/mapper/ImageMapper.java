package com.rental.transport.mapper;

import com.rental.transport.entity.ImageEntity;
import com.rental.transport.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectDeletedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageMapper {

    private final ImageRepository repository;

    public ImageEntity idToEntity(UUID id) throws ObjectDeletedException {
        return repository.getReferenceById(id);
    }

    public UUID entityToUuid(ImageEntity entity) {
        return entity.getId();
    }
}
