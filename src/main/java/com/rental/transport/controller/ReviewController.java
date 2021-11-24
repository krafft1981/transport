package com.rental.transport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

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
    public ResponseEntity<Map<String, Long>> doPostReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId,
        @RequestParam(value = "score", required = true) Long score) throws Exception {
        Map<String, Long> response =  service.addReview(principal.getName(), transportId, score);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Удаление отзыва"
    )
    @DeleteMapping
    public ResponseEntity<Map<String, Long>> doDeleteReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId) throws Exception {
        Map<String, Long> response = service.delReview(principal.getName(), transportId);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }

    @ApiOperation(
        value = "Получение отзывов"
    )
    @GetMapping
    public ResponseEntity<Map<String, Long>> doGetReview(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transportId) throws Exception {
        Map<String, Long> response = service.getScore(principal.getName(), transportId);
        return ResponseEntity
                   .ok()
                   .contentLength(objectMapper.writeValueAsBytes(response).length)
                   .body(response);
    }
}
