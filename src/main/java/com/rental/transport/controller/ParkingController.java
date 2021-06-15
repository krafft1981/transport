package com.rental.transport.controller;

import com.rental.transport.dto.Parking;
import com.rental.transport.dto.Transport;
import com.rental.transport.service.ParkingService;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/parking")
@RestController
public class ParkingController {

    @Autowired
    private ParkingService service;

    @ApiOperation(
            value = "Удаление стоянки"
    )
    @DeleteMapping
    public void doDeleteParking(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        service.delete(principal.getName(), id);
    }

    @ApiOperation(
            value = "Получение списка доступных стоянок"
    )
    @GetMapping(value = "/list")
    public List<Parking> goGetPagesParking(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }

    @ApiOperation(
            value = "Создание стоянки"
    )
    @PostMapping
    public Long goPostParking(
            Principal principal) {

        return service.create(principal.getName());
    }

    @ApiOperation(
            value = "Получение моих стоянок"
    )
    @GetMapping(value = "/my")
    public List<Parking> doGetMyParking(Principal principal) {

        return service.getMyParking(principal.getName());
    }

    @ApiOperation(
            value = "Редактирование стоянки"
    )
    @PutMapping
    public Parking goPutParking(
            Principal principal,
            @RequestBody Parking parking) {

        return service.update(principal.getName(), parking);
    }

    @ApiOperation(
            value = "Добавление картинки к стоянке"
    )
    @PostMapping(value = "/image")
    public Parking doPostParkingImage(
            Principal principal,
            @RequestParam(value = "parking_id", required = true) Long parkingId,
            @RequestBody byte[] data) {

        return service.addParkingImage(principal.getName(), parkingId, data);
    }

    @ApiOperation(
            value = "Удаление картинки из стоянки"
    )
    @DeleteMapping(value = "/image")
    public Parking doDeleteParkingImage(
            Principal principal,
            @RequestParam(value = "parking_id", required = true) Long parkingId,
            @RequestParam(value = "image_id", required = true) Long imageId) {

        return service.delParkingImage(principal.getName(), parkingId, imageId);
    }
}
