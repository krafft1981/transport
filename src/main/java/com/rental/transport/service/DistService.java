package com.rental.transport.service;

import com.google.common.io.ByteStreams;
import com.rental.transport.entity.DistEntity;
import com.rental.transport.entity.DistRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DistService {

    @Autowired
    private DistRepository distRepository;

    @Getter
    private Map<String, AtomicLong> score = new HashMap<>();

    @Value("${spring.dist.path}")
    private String distPath;

    private String buildName(String name) {
        StringBuilder builder = new StringBuilder(4096);
        builder.append(distPath).append("/").append(name);
        return builder.toString();
    }

    public Set<String> listFiles() {

        return Stream
                .of(new File(distPath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(file -> file.getName())
                .collect(Collectors.toSet());
    }

    public byte[] getFile(String name) throws IOException {
        File file = new File(buildName(name));
        Long len = file.length();
        InputStream fis = new FileInputStream(new File(buildName(name)));
        byte[] data = new byte[len.intValue()];
        ByteStreams.read(fis, data, 0, len.intValue());
        appendScore(name, 0);
        distRepository.save(new DistEntity(name));
        return data;
    }

    private void appendScore(String name, Integer start) {
        if (Objects.isNull(score.get(name)))
            score.put(name, new AtomicLong(start));
        else
            score.get(name).incrementAndGet();
    }

    @PostConstruct
    public void postConstruct() {
        distRepository
                .findAll()
                .forEach(entity -> appendScore(entity.getProgram(), 1));
    }
}
