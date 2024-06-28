package com.akshit.treading.controller;

import com.akshit.treading.config.JwtProvider;
import com.akshit.treading.modal.TwoFactorOTP;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.UserRepository;
import com.akshit.treading.response.AuthResponse;
import com.akshit.treading.service.CustomUserDetailsService;
import com.akshit.treading.service.EmailService;
import com.akshit.treading.service.TwoFactorOTPService;
import com.akshit.treading.service.WatchlistService;
import com.akshit.treading.utils.OTPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOTPService twoFactorOTPService;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if(isEmailExist != null){
            throw new Exception("email already Exist Sign Up with another account");
        }


        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User saved = userRepository.save(newUser);
        watchlistService.createWatchlist(saved);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("User Register Successfully");
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        String userName = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(userName);
//        if(user.getTwoFactorAuth().isEnabled()){
//            AuthResponse res = new AuthResponse();
//            res.setMessage("Two factor Authentication is Enabled");
//            res.setIsTwoFactorAuthEnabled(true);
//            String otp = OTPUtils.generateOtp();
//
//            TwoFactorOTP oldTwoFactorOtp = twoFactorOTPService.findByUserId(authUser.getId());
//
//            if(oldTwoFactorOtp != null) twoFactorOTPService.deleteTwoFactorOtp(oldTwoFactorOtp);
//
//            TwoFactorOTP newTwoFactorOtp = twoFactorOTPService.createTwoFactorOtp(authUser,otp,jwt);
//
//            emailService.sandVerificationOtpEmail(userName,otp);
//            res.setSession(newTwoFactorOtp.getId());
//            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
//        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("User Login Successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid UserName");
        }

        if(!password.equals(userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp,@RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOTPService.findById(id);
        if(twoFactorOTPService.verifyTwoFactorOtp(twoFactorOTP,otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor Authentication Done");
            res.setIsTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        throw new Exception("invalid OTP");
    }
}
