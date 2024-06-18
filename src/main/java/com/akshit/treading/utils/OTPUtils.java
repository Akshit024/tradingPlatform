package com.akshit.treading.utils;

import java.util.Random;

public class OTPUtils {

    public static String generateOtp(){
        int otpLength = 6;
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<otpLength;i++){
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
