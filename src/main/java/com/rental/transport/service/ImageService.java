package com.rental.transport.service;

import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

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
        Long count = 0L;
        for (byte b : image) { count++; }
        System.out.println(image.length + " " + count);
        return new String(image);
    }
}
