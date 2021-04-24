package com.rental.transport.service;

import com.rental.transport.dto.Image;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.mapper.ImageMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
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

    public Long create(String source) {

        ImageEntity entity = new ImageEntity(source);
        return imageRepository.save(entity).getId();
    }

    public void delete(List<Long> ids) {

        ids.stream().forEach(id -> imageRepository.deleteById(id));
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
                .map(entity -> entity.getId())
                .collect(Collectors.toList());

        return result;
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
