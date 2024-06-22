package com.akshit.treading.service.Implementation;

import com.akshit.treading.domain.PaymentMethod;
import com.akshit.treading.domain.PaymentOrderStatus;
import com.akshit.treading.modal.PaymentOrder;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.PaymentOrderRepository;
import com.akshit.treading.response.PaymentResponse;
import com.akshit.treading.service.PaymentOrderService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${stripe.api.secret}")
    private String apiSecretKey;


    @Override
    public PaymentOrder createOrder(User user, double amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        Optional<PaymentOrder> opt = paymentOrderRepository.findById(id);
        if(opt.isEmpty()) throw  new Exception("Payment Order not exist");
        return opt.get();
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                String status = payment.get("status");
                if(status.equals("captured")){
                    paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            else if(paymentOrder.getPaymentMethod().equals(PaymentMethod.STRIPE)){

            }
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorPayPaymentLink(User user, double amount) throws RazorpayException {
        Long Amount = (long)(amount*100);
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecretKey);
            JSONObject paymentLinkObject = new JSONObject();
            paymentLinkObject.put("amount",Amount);
            paymentLinkObject.put("currency","INR");

            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());
            paymentLinkObject.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkObject.put("notify",notify);

            paymentLinkObject.put("reminder_enable",true);
            paymentLinkObject.put("callback_url","http://localhost:5173/wallet");
            paymentLinkObject.put("callback_method","ret");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkObject);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);

            return res;

        }catch (Exception e){
            System.out.println("Error Creating Payment Link : " + e.getMessage());
            throw  new RazorpayException(e.getMessage());
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, double amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams sessionCreateParams = new SessionCreateParams.Builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long)amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("Top up wallet").build()).build()
                        ).build()
                ).build();

        Session session = Session.create(sessionCreateParams);

        System.out.println("session ----- "+session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());
        return res;
    }
}
