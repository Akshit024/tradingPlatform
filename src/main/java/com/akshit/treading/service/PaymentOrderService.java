package com.akshit.treading.service;

import com.akshit.treading.domain.PaymentMethod;
import com.akshit.treading.modal.PaymentOrder;
import com.akshit.treading.modal.User;
import com.akshit.treading.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentOrderService {
    PaymentOrder createOrder(User user, double amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;

    PaymentResponse createRazorPayPaymentLink(User user,double amount) throws RazorpayException;

    PaymentResponse createStripePaymentLink(User user,double amount,Long orderId) throws StripeException;

}
