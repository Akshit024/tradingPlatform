package com.akshit.treading.controller;

import com.akshit.treading.domain.VerificationType;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.VerificationCode;
import com.akshit.treading.service.EmailService;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt,@PathVariable String verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode == null){
            verificationCode = verificationCodeService.sendVerificationCode(user, VerificationType.valueOf(verificationType));
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sandVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
        }

        return new ResponseEntity<>("verification Otp Send Successfully", HttpStatus.OK);
    }
    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,@PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sentTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
                ?verificationCode.getEmail():verificationCode.getMobile();
        boolean isVerified = verificationCode.getOtp().equals(otp);
        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),sentTo,user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }
}
