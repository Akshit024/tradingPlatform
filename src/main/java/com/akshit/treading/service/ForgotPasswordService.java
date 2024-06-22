package com.akshit.treading.service;

import com.akshit.treading.domain.VerificationType;
import com.akshit.treading.modal.ForgotPasswordToken;
import com.akshit.treading.modal.User;
import org.springframework.stereotype.Service;

@Service
public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sentTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteForgotPasswordToken(ForgotPasswordToken forgotPasswordToken);
}
