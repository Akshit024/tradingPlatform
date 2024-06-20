package com.akshit.treading.controller;

import com.akshit.treading.domain.VerificationType;
import com.akshit.treading.modal.ForgotPasswordToken;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.VerificationCode;
import com.akshit.treading.request.ForgotPasswordTokenRequest;
import com.akshit.treading.request.ResetPasswordRequest;
import com.akshit.treading.response.ApiResponse;
import com.akshit.treading.response.AuthResponse;
import com.akshit.treading.service.EmailService;
import com.akshit.treading.service.ForgotPasswordService;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.VerificationCodeService;
import com.akshit.treading.utils.OTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt,@PathVariable String verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode == null)
            verificationCode = verificationCodeService.sendVerificationCode(user, VerificationType.valueOf(verificationType));

        return new ResponseEntity<>("verification Otp Send Successfully", HttpStatus.OK);
    }
    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
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

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendVerificationOtp(@RequestBody ForgotPasswordTokenRequest forgotPasswordTokenRequest) throws Exception {
        User user = userService.findUserProfileByEmail(forgotPasswordTokenRequest.getSentTo());
        String otp = OTPUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUser(user.getId());
        if(forgotPasswordToken == null){
            forgotPasswordToken = forgotPasswordService.createToken(user,id,otp,forgotPasswordTokenRequest.getVerificationType(),forgotPasswordTokenRequest.getSentTo());
        }

        if(forgotPasswordTokenRequest.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sandVerificationOtpEmail(forgotPasswordTokenRequest.getSentTo(),forgotPasswordToken.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(forgotPasswordToken.getId());
        response.setMessage("Password Send Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> verifyResetPassword(@RequestParam String id, @RequestBody ResetPasswordRequest resetPasswordRequest) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        boolean isVerified = forgotPasswordToken.getOtp().equals(resetPasswordRequest.getOtp());

        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(), resetPasswordRequest.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password Update Successfully");
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        throw new Exception("Invalid OTP");
    }




}
