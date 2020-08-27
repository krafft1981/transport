package com.rental.transport.controller;

import com.rental.transport.dao.AbstractEntity;
import com.rental.transport.dao.AbstractRepository;
import com.rental.transport.dto.AbstractDto;
import com.rental.transport.service.AbstractMapper;
import com.rental.transport.service.AbstractService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController<
        E extends AbstractEntity,
        D extends AbstractDto,
        R extends AbstractRepository<E>,
        M extends AbstractMapper<E, D>,
        S extends AbstractService<E, R, M, D>>
        implements AbstractInterface<D> {

    protected final S service;

    @Autowired
    protected AbstractController(S service) {
        this.service = service;
    }

    @GetMapping(value = "/count")
    public Long getCount() {
        return service.count();
    }

    @GetMapping(value = "/count/my")
    public Long getMyCount(Principal principal) {
        return service.count(principal);
    }

    @DeleteMapping
    public void delete(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {
        service.delete(principal, id);
    }

    @PutMapping
    public void update(Principal principal,
                       @RequestBody D dto) {
        service.update(principal, dto);
    }

    @GetMapping(value = "/list")
    public List<D> getPages(@RequestParam(value = "page", required = true) Integer page,
                           @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @GetMapping(value = "/list/my")
    public List<D> getMyPages(Principal principal,
                           @RequestParam(value = "page", required = true) Integer page,
                           @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(principal, pageable);
    }

    @GetMapping
    public D findById(Principal principal,
                      @RequestParam(value = "id", required = true) Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Long create(Principal principal) {
        return service.create(principal);
    }
}
