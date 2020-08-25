package com.rental.transport.controller;

import com.rental.transport.dto.AbstractDto;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface AbstractInterface<D extends AbstractDto> {

    @GetMapping
    public Long getCount();

    @GetMapping
    public Long getMyCount(Principal principal);

    @DeleteMapping
    public void delete(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id);

    @PutMapping
    public void update(Principal principal,
                       @RequestBody D dto);

    @GetMapping
    public List<D> getPages(@RequestParam(value = "page", required = true) Integer page,
                           @RequestParam(value = "size", required = true) Integer size);

    @GetMapping
    public List<D> getMyPages(Principal principal,
                           @RequestParam(value = "page", required = true) Integer page,
                           @RequestParam(value = "size", required = true) Integer size);

    @GetMapping
    public D findById(Principal principal,
                      @RequestParam(value = "id", required = true) Long id);

    @PostMapping
    public Long create(Principal principal);
}
