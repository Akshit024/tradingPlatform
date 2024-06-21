package com.akshit.treading.modal;

import com.akshit.treading.domain.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus status;

    private double amount;

    @ManyToOne
    private User user;

    private LocalDateTime date = LocalDateTime.now();

}
