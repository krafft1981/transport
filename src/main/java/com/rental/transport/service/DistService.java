package com.rental.transport.service;

import com.google.common.io.ByteStreams;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DistService {

    private Map<String, AtomicLong> score = new HashMap<>();

    @Value("${spring.dist.path}")
    private String distPath;

    private String buildName(String name) {
        StringBuilder builder = new StringBuilder(1024);
        builder.append(distPath).append("/").append(name);
        return builder.toString();
    }

    public Set<String> listFiles() {
        return Stream.of(new File(distPath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public byte[] getFile(String name) throws IOException {
        File file = new File(buildName(name));
        Integer len = Long.valueOf(file.length()).intValue();
        byte[] data = new byte[len];
        InputStream is = new FileInputStream(file);
        ByteStreams.read(is, data, 0, len);
        appendScore(name);
        return data;
    }

    private void appendScore(String name) {
        if (Objects.isNull(score.get(name)))
            score.put(name, new AtomicLong(1));
        else {
            score.get(name).incrementAndGet();
        }
    }

    public Map<String, AtomicLong> readScore() {

        return score;
    }
}
