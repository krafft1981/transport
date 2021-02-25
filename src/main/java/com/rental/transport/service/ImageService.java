package com.rental.transport.service;

import com.rental.transport.dto.Image;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.mapper.ImageMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    public Long create(@NonNull String image) {

        ImageEntity entity = new ImageEntity(image);
        Long id = imageRepository.save(entity).getId();
        return id;
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public void delete(List<Long> ids) {

        ids.stream().forEach(id -> {
            imageRepository.deleteById(id);
        });
    }

    public Image getImage(Long id) throws ObjectNotFoundException {

        ImageEntity entity = getEntity(id);
        return imageMapper.toDto(entity);
    }

    public List<Long> getPage(Pageable pageable) {

        List<Long> result = imageRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return entity.getId();
                })
                .collect(Collectors.toList());

        return result;
    }

    public Long count() {

        return imageRepository.count();
    }

    public byte[] Base64DecodeImage(Image image) {
        byte[] base64Bytes = image.getData().getBytes();
        byte[] decodedBytes = Base64.decode(base64Bytes);
        return decodedBytes;
    }

    public ImageEntity getEntity(Long id) throws ObjectNotFoundException {

        return imageRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Image", id));
    }

    public Image getDto(Long id) throws ObjectNotFoundException {

        return imageMapper.toDto(getEntity(id));
    }
}
