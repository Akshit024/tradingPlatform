package com.akshit.treading.modal;

import com.akshit.treading.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = true;
    private VerificationType sentTo;
}
