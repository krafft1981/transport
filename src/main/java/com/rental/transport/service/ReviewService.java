package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ReviewEntity;
import com.rental.transport.entity.ReviewRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReviewService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private ReviewRepository reviewRepository;

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

        List<ReviewEntity> reviews = reviewRepository.findByTransport(transport);

        Map<String, Long> result = new HashMap();
        result.put("review", 0L);
        result.put("score", 0L);
        result.put("total", Long.valueOf(reviews.size()));

        if (reviews.isEmpty())
            return result;

        AtomicLong sum = new AtomicLong(0L);
        reviews.parallelStream().forEach(entity -> sum.addAndGet(entity.getScore()));

        result.put("score", Long.valueOf(Math.round(Math.floor(sum.get() / reviews.size()))));
        ReviewEntity review = reviewRepository.findByCustomerAndTransport(customer, transport);
        if (Objects.nonNull(review))
            result.put("review", review.getScore());

        return result;
    }
}
