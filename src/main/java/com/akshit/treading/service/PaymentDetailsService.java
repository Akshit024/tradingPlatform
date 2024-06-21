package com.akshit.treading.service;

import com.akshit.treading.modal.PaymentDetails;
import com.akshit.treading.modal.User;

public interface PaymentDetailsService {

    PaymentDetails addPayment(String accountNumber, String name, String ifsc, String bankName, User user);
    PaymentDetails getUserPaymentDetails(User user);
}
