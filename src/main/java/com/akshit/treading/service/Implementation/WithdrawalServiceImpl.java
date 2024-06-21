package com.akshit.treading.service.Implementation;

import com.akshit.treading.domain.WithdrawalStatus;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Withdrawal;
import com.akshit.treading.repository.WithdrawalRepository;
import com.akshit.treading.service.WithdrawalService;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(double amount, User user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUser(user);
        withdrawal.setAmount(amount);
        withdrawal.setStatus(WithdrawalStatus.PENDING);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if(withdrawal.isEmpty()) throw new Exception("Withdrawal Not Exist");
        Withdrawal withdrawal1 = withdrawal.get();
        withdrawal1.setDate(LocalDateTime.now());
        if(accept){
            withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
        }else {
            withdrawal1.setStatus(WithdrawalStatus.PENDING);
        }
        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawal() {
        return withdrawalRepository.findAll();
    }
}
