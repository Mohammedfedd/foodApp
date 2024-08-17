package com.foodApp.service;

import com.foodApp.model.Order;
import com.foodApp.response.PaymentResponse;
import com.stripe.exception.StripeException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentService {
    public PaymentResponse createPaymentLink(Order order) throws StripeException;
}
