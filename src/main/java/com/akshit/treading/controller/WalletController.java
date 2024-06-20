package com.akshit.treading.controller;

import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.modal.WalletTransaction;
import com.akshit.treading.service.OrderService;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt, @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.findWalletById(walletId);
        Wallet senderWallet = walletService.transferFunds(user,wallet,req.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

}
