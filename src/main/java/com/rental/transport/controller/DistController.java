package com.rental.transport.controller;

import com.rental.transport.service.DistService;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/dist")
@RestController
@AllArgsConstructor
public class DistController {

    private final DistService distService;

    @ApiOperation(
        value = "Получение списка доступных файлов"
    )
    @GetMapping(value = "/list")
    public Set<String> getDistList() {
        return distService.listFiles();
    }

    @ApiOperation(
        value = "Получение файла"
    )
    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<byte[]> getDistFile(
        @RequestParam(value = "name", required = true) String name
    ) throws IOException {

        HttpHeaders header = new HttpHeaders();
        header.set("Content-type", "application/octet-stream");
        header.set("Content-Disposition", "attachment; filename=" + name);
        return new HttpEntity(distService.getFile(name), header);
    }

    @ApiOperation(
        value = "Получение количества загрузок"
    )
    @GetMapping(value = "/score")
    public Map<String, AtomicLong> getDistScore() {
        return distService.getScore();
    }
}
