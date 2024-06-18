package com.akshit.treading.modal;

import com.akshit.treading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private String sentTo;

    private VerificationType verificationType;

}
