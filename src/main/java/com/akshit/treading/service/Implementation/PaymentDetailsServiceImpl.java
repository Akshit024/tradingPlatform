package com.akshit.treading.service.Implementation;

import com.akshit.treading.modal.PaymentDetails;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.PaymentDetailsRepository;
import com.akshit.treading.service.PaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentDetailsServiceImpl implements PaymentDetailsService {
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPayment(String accountNumber, String name, String ifsc, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setUser(user);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(name);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
