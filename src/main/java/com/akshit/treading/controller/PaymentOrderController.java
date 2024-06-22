package com.akshit.treading.controller;

import com.akshit.treading.domain.PaymentMethod;
import com.akshit.treading.modal.PaymentOrder;
import com.akshit.treading.modal.User;
import com.akshit.treading.response.PaymentResponse;
import com.akshit.treading.service.PaymentOrderService;
import com.akshit.treading.service.UserService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentOrderController {
    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private UserService userService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(@RequestHeader("Authorization") String jwt, @PathVariable PaymentMethod paymentMethod,@PathVariable double amount) throws Exception, RazorpayException, StripeException {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse;

        PaymentOrder order = paymentOrderService.createOrder(user,amount,paymentMethod);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentOrderService.createRazorPayPaymentLink(user,amount,order.getId());
        }else{
            paymentResponse =paymentOrderService.createStripePaymentLink(user,amount,order.getId());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
