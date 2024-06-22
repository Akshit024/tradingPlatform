package com.akshit.treading.repository;

import com.akshit.treading.modal.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
    PaymentDetails findByUserId(Long userId);
}
