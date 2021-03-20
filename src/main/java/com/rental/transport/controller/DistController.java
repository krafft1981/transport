package com.rental.transport.controller;

import com.rental.transport.service.DistService;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/dist")
@RestController
public class DistController {

    @Autowired
    private DistService distService;

    @GetMapping(value = "/list")
    public Set<String> getDistList() throws IOException {
        return distService.listFiles();
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<byte[]> getDistFile(
            @RequestParam(value = "name", required = true) String name
    ) throws IOException {

        HttpHeaders header = new HttpHeaders();
        header.set("Content-type", "application/octet-stream");
        header.set("Content-Disposition", "attachment; filename=" + name);
        return new HttpEntity<byte[]>(distService.getFile(name), header);
    }

    @GetMapping(value = "/score")
    public Map<String, AtomicLong> getDistScore() {

        return distService.readScore();
    }
}