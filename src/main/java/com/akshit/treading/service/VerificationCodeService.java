package com.akshit.treading.service;

import com.akshit.treading.domain.VerificationType;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.VerificationCode;
import org.springframework.stereotype.Service;


public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCode(VerificationCode verificationCode);

}
