package com.akshit.treading.service.Implementation;

import com.akshit.treading.domain.WalletTransactionType;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.modal.WalletTransaction;
import com.akshit.treading.repository.WalletTransactionRepository;
import com.akshit.treading.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletTransaction createTransaction(Wallet wallet, WalletTransactionType walletTransactionType, Long receiverId, String purpose, double amount) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setWalletTransactionType(walletTransactionType);
        walletTransaction.setDate(LocalDate.now());
        walletTransaction.setPurpose(purpose);
        walletTransaction.setAmount(amount);
        walletTransaction.setTransferId(receiverId);
        return walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {
        return walletTransactionRepository.findByWalletId(wallet.getId());
    }
}
