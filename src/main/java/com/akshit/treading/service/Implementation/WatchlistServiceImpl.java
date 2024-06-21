package com.akshit.treading.service.Implementation;

import com.akshit.treading.modal.*;
import com.akshit.treading.repository.WatchlistRepository;
import com.akshit.treading.service.WalletService;
import com.akshit.treading.service.WatchlistService;
import com.akshit.treading.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if(watchlist == null) throw new Exception("WatchList Not Exist");
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if(watchlist.isEmpty()) throw new Exception("WatchList Not Exist");
        return watchlist.get();
    }

    @Override
    public Coin addCoinToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchlist(user.getId());
        if(watchlist.getCoins().contains(coin)){
            watchlist.getCoins().remove(coin);
        }
        else{
            watchlist.getCoins().add(coin);
        }
        watchlistRepository.save(watchlist);
        return coin;
    }
}
