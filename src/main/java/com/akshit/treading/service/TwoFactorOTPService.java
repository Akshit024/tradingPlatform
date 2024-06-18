package com.akshit.treading.service;

import com.akshit.treading.modal.TwoFactorOTP;
import com.akshit.treading.modal.User;

public interface TwoFactorOTPService {
    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUserId(Long userId);

    TwoFactorOTP findById(String Id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
