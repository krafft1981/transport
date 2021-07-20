package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ReviewEntity;
import com.rental.transport.entity.ReviewRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Long addReview(String account, Long transportId, Long score) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        if (Objects.isNull(reviewRepository.findByCustomerAndTransport(customer, transport))) {
            ReviewEntity review = new ReviewEntity(customer, transport, score);
            reviewRepository.save(review);
        }
        return getScore(customer, transport);
    }

    public Long delReview(String account, Long transportId) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        reviewRepository.deleteByCustomerAndTransport(customer, transport);
        return getScore(customer, transport);
    }

    public Long getScore(String account, Long transportId) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        return getScore(customer, transport);
    }

    private Long getScore(CustomerEntity customer, TransportEntity transport) throws ObjectNotFoundException {

        List<ReviewEntity> reviews = reviewRepository.findByTransport(transport);

        if (reviews.isEmpty())
            return -1L;

        AtomicLong sum = new AtomicLong(0L);
        reviews.parallelStream().forEach(entity -> sum.addAndGet(entity.getScore()));

        Long result = Long.valueOf(Math.round(sum.get() / reviews.size()));
        if (Objects.isNull(reviewRepository.findByCustomerAndTransport(customer, transport)))
            result *= -1;

        return result;
    }
}
