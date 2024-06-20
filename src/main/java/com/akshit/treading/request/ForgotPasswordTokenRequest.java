package com.akshit.treading.request;

import com.akshit.treading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    VerificationType verificationType;
    String sentTo;
}
