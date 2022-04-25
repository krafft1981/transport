package com.rental.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/review")
@RestController
public class ReviewController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewService service;

    @ApiOperation(
        value = "Добавление отзыва"
    )
    @PostMapping
    public Map<String, Long> doPostReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId,
        @RequestParam(value = "score", required = true) Long score) throws Exception {
        return service.addReview(principal.getName(), transportId, score);
    }

    @ApiOperation(
        value = "Удаление отзыва"
    )
    @DeleteMapping
    public Map<String, Long> doDeleteReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId) throws Exception {
        return service.delReview(principal.getName(), transportId);
    }

    @ApiOperation(
        value = "Получение отзывов"
    )
    @GetMapping
    public Map<String, Long> doGetReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId) throws Exception {
        return service.getScore(principal.getName(), transportId);
    }
}
