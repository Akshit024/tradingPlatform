package com.akshit.treading.service.Implementation;

import com.akshit.treading.modal.TwoFactorOTP;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.TwoFactorOTPRepository;
import com.akshit.treading.service.TwoFactorOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService {

    @Autowired
    private TwoFactorOTPRepository twoFactorOTPRepository;


    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOTP newTwoFactorOTP = new TwoFactorOTP();

        newTwoFactorOTP.setId(id);
        newTwoFactorOTP.setUser(user);
        newTwoFactorOTP.setOtp(otp);
        newTwoFactorOTP.setJwt(jwt);

        return twoFactorOTPRepository.save(newTwoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUserId(Long userId) {
        return twoFactorOTPRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        return twoFactorOTPRepository.findById(id).orElse(null);
    }


    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOTPRepository.delete(twoFactorOTP);
    }
}
