package com.foodApp.service;

import com.foodApp.model.Order;
import com.foodApp.model.User;
import com.foodApp.repository.OrderRepository;
import com.foodApp.repository.UserRepository;
import com.foodApp.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Constructor injection for repositories
    public PaymentServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentResponse createPaymentLink(Order order) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:3000/payment/fail")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("mad")
                                .setUnitAmount((long) order.getTotalPrice() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Tasty Dash")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());

        return res;
    }

    // New method to handle payment success
    public void handlePaymentSuccess(String sessionId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // Fetch session from Stripe
        Session session = Session.retrieve(sessionId);

        // Verify the payment was successful
        if ("complete".equals(session.getPaymentStatus())) {
            // Retrieve the order associated with this session
            Order order = retrieveOrderBySessionId(sessionId);

            if (order != null) {
                // Update the order status to "PENDING"
                order.setOrderStatus("PENDING");
                orderRepository.save(order);
            }
        } else {
            // Handle payment failure
            // Optionally, update the order status or notify the user
        }
    }

    // Method to retrieve the order by session ID (this needs to be implemented)
    private Order retrieveOrderBySessionId(String sessionId) {
        // Retrieve the order associated with the session ID
        // You need to implement this method based on your order management logic
        return null; // Replace with actual implementation
    }
}
