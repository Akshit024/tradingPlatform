package com.akshit.treading.controller;

import com.akshit.treading.domain.WalletTransactionType;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.modal.WalletTransaction;
import com.akshit.treading.modal.Withdrawal;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.WalletService;
import com.akshit.treading.service.WalletTransactionService;
import com.akshit.treading.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.dsig.TransformService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletTransactionService walletTransactionService;


    @PostMapping("/withdrawal/amount")
    public ResponseEntity<?> withdrawalRequest(@PathVariable double amount, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(wallet,-withdrawal.getAmount());

        WalletTransaction walletTransaction = walletTransactionService.createTransaction(wallet, WalletTransactionType.WITHDRAWAL,null,"bank account withdrawal",withdrawal.getAmount());
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id,@PathVariable boolean accept,@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.procedWithdrawal(id,accept);
        Wallet wallet = walletService.getUserWallet(user);

        if(!accept){
            walletService.addBalance(wallet,withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }

    @GetMapping("/withdrawal")
    public ResponseEntity<?> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> list = withdrawalService.getWithdrawalHistory(user);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/admin/withdrawal")
    public ResponseEntity<?> getAllWithdrawal() throws Exception {
        List<Withdrawal> list = withdrawalService.getAllWithdrawal();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }




}
