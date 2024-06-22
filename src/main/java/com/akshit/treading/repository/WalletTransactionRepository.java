package com.akshit.treading.repository;

import com.akshit.treading.modal.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {
    List<WalletTransaction> findByWalletId(Long walletId);
}
