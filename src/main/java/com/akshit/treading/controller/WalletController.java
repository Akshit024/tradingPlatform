package com.akshit.treading.controller;

import com.akshit.treading.domain.WalletTransactionType;
import com.akshit.treading.modal.*;
import com.akshit.treading.response.PaymentResponse;
import com.akshit.treading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @Autowired
    private WalletTransactionService walletTransactionService;

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
        walletTransactionService.createTransaction(senderWallet, WalletTransactionType.WALLET_TRANSACTION,wallet.getId(),req.getPurpose(),req.getAmount());
        return new ResponseEntity<>(senderWallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order,user);
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(@RequestHeader("Authorization") String jwt,
                                                     @RequestParam(name = "order_id") Long orderId,
                                                     @RequestParam(name = "payment_id") String paymentId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        PaymentOrder order = paymentOrderService.getPaymentOrderById(orderId);

        Boolean status = paymentOrderService.proceedPaymentOrder(order,paymentId);
        if(wallet.getBalance()==null){
            wallet.setBalance(BigDecimal.valueOf(0));
        }
        if(status){
            wallet = walletService.addBalance(wallet,order.getAmount());
        }
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

}
