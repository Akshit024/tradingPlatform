package com.akshit.treading.service;

import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Withdrawal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface WithdrawalService {

    Withdrawal requestWithdrawal(double amount, User user);
    Withdrawal procedWithdrawal(Long withdrawalId,boolean accept) throws Exception;
    List<Withdrawal> getWithdrawalHistory(User user);
    List<Withdrawal> getAllWithdrawal();
}
