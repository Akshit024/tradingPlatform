package com.akshit.treading.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private boolean status;
    private String message;
    private boolean IsTwoFactorAuthEnabled;
    private String session;
}
