package com.akshit.treading.modal;

import com.akshit.treading.domain.PaymentMethod;
import com.akshit.treading.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private PaymentMethod paymentMethod;

    private PaymentOrderStatus paymentOrderStatus;

    private double amount;

    @ManyToOne
    private User user;
}
