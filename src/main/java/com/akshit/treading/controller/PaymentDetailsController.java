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
        PaymentDetails paymentDetails1 = paymentDetailsService.addPayment(paymentDetails.getAccountNumber(),paymentDetails.getAccountHolderName(),paymentDetails.getIfsc(),paymentDetails.getBankName(),user);
        return new ResponseEntity<>(paymentDetails1, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);
        return  new ResponseEntity<>(paymentDetails,HttpStatus.OK);
    }
}
