package com.akshit.treading.service;

import com.akshit.treading.modal.PaymentDetails;
import com.akshit.treading.modal.User;
import org.springframework.stereotype.Service;

@Service
public interface PaymentDetailsService {

    PaymentDetails addPayment(String accountNumber, String name, String ifsc, String bankName, User user);
    PaymentDetails getUserPaymentDetails(User user);
}
