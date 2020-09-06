package com.rental.transport.service;

import com.rental.transport.dto.Image;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.mapper.ImageMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper mapper;

    public Long create(@NonNull String image) {

        ImageEntity entity = new ImageEntity(image);
        Long id = imageRepository.save(entity).getId();
        return id;
    }

    public void delete(List<Long> ids) {

        ids.stream().forEach(id -> { imageRepository.deleteById(id); });
    }

    public Image getImage(Long id) throws ObjectNotFoundException {

        ImageEntity entity = imageRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Картинка", id));

        return mapper.toDto(entity);
    }

    public List<Image> getPage(Pageable pageable) {

        List<Image> result = imageRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

        return result;
    }
}
