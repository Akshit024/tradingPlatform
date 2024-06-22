package com.akshit.treading.service;

import com.akshit.treading.domain.WalletTransactionType;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.modal.WalletTransaction;

import java.util.List;

public interface WalletTransactionService {
    WalletTransaction createTransaction(Wallet wallet, WalletTransactionType walletTransactionType,Long receiverId,String purpose,double amount);
    List<WalletTransaction> getTransactionsByWallet(Wallet wallet);
}
