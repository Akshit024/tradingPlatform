package com.akshit.treading.controller;

import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.modal.WalletTransaction;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.WalletService;
import com.akshit.treading.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class WalletTransactionController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletTransactionService walletTransactionService;

    @GetMapping
    public ResponseEntity<List<WalletTransaction>> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        List<WalletTransaction> list = walletTransactionService.getTransactionsByWallet(wallet);
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }
}
