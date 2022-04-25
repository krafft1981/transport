package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ReviewEntity;
import com.rental.transport.entity.ReviewRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReviewService {

    private final CustomerService customerService;
    private final TransportService transportService;
    private final ReviewRepository reviewRepository;

    public Map<String, Long> addReview(String account, Long transportId, Long score) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        if (Objects.isNull(reviewRepository.findByCustomerAndTransport(customer, transport))) {
            ReviewEntity review = new ReviewEntity(customer, transport, score);
            reviewRepository.save(review);
        }
        return getScore(customer, transport);
    }

    public Map<String, Long> delReview(String account, Long transportId) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        ReviewEntity review = reviewRepository.findByCustomerAndTransport(customer, transport);
        if (Objects.nonNull(review))
            reviewRepository.delete(review);
        return getScore(customer, transport);
    }

    public Map<String, Long> getScore(String account, Long transportId) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        return getScore(customer, transport);
    }

    private Map<String, Long> getScore(CustomerEntity customer, TransportEntity transport) throws ObjectNotFoundException {

        Long count = reviewRepository.findCountByTransport(transport.getId());
        Long summ = reviewRepository.findSumByTransport(transport.getId());

        Map<String, Long> result = new HashMap<>();
        result.put("score", 0L);
        result.put("total", count);

        if (count == 0L)
            return result;

        result.put("score", Math.round(Math.floor(summ / count)));
        ReviewEntity review = reviewRepository.findByCustomerAndTransport(customer, transport);
        if (Objects.nonNull(review))
            result.put("review", review.getScore());

        return result;
    }
}
