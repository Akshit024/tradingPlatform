package com.akshit.treading.service;

import com.akshit.treading.modal.Order;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Wallet;


public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long amount);
    Wallet findWalletById(Long id) throws Exception;
    Wallet transferFunds(User sender,Wallet receiver,Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
