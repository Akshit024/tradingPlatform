package com.akshit.treading.controller;

import com.akshit.treading.modal.PaymentDetails;
import com.akshit.treading.modal.User;
import com.akshit.treading.service.PaymentDetailsService;
import com.akshit.treading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetails, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails1 = new PaymentDetails();
        paymentDetails1.setAccountHolderName(paymentDetails.getAccountHolderName());
        paymentDetails1.setUser(user);
        paymentDetails1.setIfsc(paymentDetails.getIfsc());
        paymentDetails1.setBankName(paymentDetails.getBankName());
        paymentDetails1.setAccountNumber(paymentDetails.getAccountNumber());
        return new ResponseEntity<>(paymentDetails1, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);
        return  new ResponseEntity<>(paymentDetails,HttpStatus.FOUND);
    }
}
