package com.rental.transport.service;

import com.rental.transport.entity.ImageEntity;
import com.rental.transport.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public UUID create(byte[] data) {

        ImageEntity entity = new ImageEntity(data);
        return imageRepository.save(entity).getId();
    }

    public void delete(List<UUID> ids) {

        ids.forEach(imageRepository::deleteById);
    }

    public byte[] getImage(UUID id) {
        return getEntity(id).getBody();
    }

    public List<UUID> getPage(Pageable pageable) {

        return imageRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(ImageEntity::getId)
                .toList();
    }

    public ImageEntity getEntity(UUID id) {
        return imageRepository.getReferenceById(id);
    }

    public String getStringImage(UUID id) {

        byte[] image = getImage(id);
        Long count = 0L;
        for (byte b : image) { count++; }
        return new String(image);
    }
}
