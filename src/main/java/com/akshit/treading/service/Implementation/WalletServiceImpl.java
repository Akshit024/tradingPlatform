package com.akshit.treading.service.Implementation;

import com.akshit.treading.domain.OrderType;
import com.akshit.treading.modal.Order;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Wallet;
import com.akshit.treading.repository.WalletRepository;
import com.akshit.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null){
            wallet=new Wallet();
            wallet.setUser(user);
        }

        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, double amount) {
        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(amount)));
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> opt = walletRepository.findById(id);
        if(opt.isEmpty()) throw new Exception("Wallet Not Found");
        return opt.get();
    }

    @Override
    public Wallet transferFunds(User sender, Wallet receiver, double amount) throws Exception {
        Wallet wallet = getUserWallet(sender);
        if(wallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0) throw new Exception("Insufficient Balance ........");
        wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(amount)));
        addBalance(receiver,amount);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = walletRepository.findByUserId(user.getId());

        if(order.getOrderType().equals(OrderType.BUY)){
            if(wallet.getBalance().compareTo(order.getPrice())<0) throw new Exception("Insufficient Balance ........");
            wallet.setBalance(wallet.getBalance().subtract(order.getPrice()));
        }else{
            wallet.setBalance(wallet.getBalance().add(order.getPrice()));
        }

        return walletRepository.save(wallet);
    }
}
