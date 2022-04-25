package com.rental.transport.service;

import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Long create(byte[] data) {

        ImageEntity entity = new ImageEntity(data);
        return imageRepository.save(entity).getId();
    }

    public void delete(List<Long> ids) {

        ids.forEach(id -> imageRepository.deleteById(id));
    }

    public byte[] getImage(Long id) throws ObjectNotFoundException {

        ImageEntity entity = getEntity(id);
        return entity.getData();
    }

    public List<Long> getPage(Pageable pageable) {

        return imageRepository
                   .findAll(pageable)
                   .getContent()
                   .parallelStream()
                   .map(ImageEntity::getId)
                   .collect(Collectors.toList());
    }

    public ImageEntity getEntity(Long id) throws ObjectNotFoundException {

        return imageRepository
                   .findById(id)
                   .orElseThrow(() -> new ObjectNotFoundException("Image", id));
    }

    public String getStringImage(Long id) throws ObjectNotFoundException {

        byte[] image = getImage(id);
        StringBuilder builder = new StringBuilder(image.length);
        for (byte c : image) {
            char v = Character.forDigit(c, 10);
            builder.append(v);
        }

        return builder.toString();
    }
}
