package com.akshit.treading.service;

import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(User user);
    Watchlist findById(Long id) throws Exception;
    Coin addCoinToWatchlist(Coin coin,User user) throws Exception;
}
