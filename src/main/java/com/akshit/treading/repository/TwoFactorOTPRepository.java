package com.akshit.treading.repository;

import com.akshit.treading.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOTPRepository extends JpaRepository<TwoFactorOTP,String> {
    TwoFactorOTP findByUserId(Long userId);
}
