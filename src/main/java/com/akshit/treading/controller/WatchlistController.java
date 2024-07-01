package com.akshit.treading.controller;

import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.User;
import com.akshit.treading.modal.Watchlist;
import com.akshit.treading.service.CoinService;
import com.akshit.treading.service.UserService;
import com.akshit.treading.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
        return new ResponseEntity<>(watchlist, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Watchlist> createWatchlist(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.createWatchlist(user);
        return new ResponseEntity<>(watchlist, HttpStatus.CREATED);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getUserWatchList(@PathVariable Long watchlistId) throws Exception {
        Watchlist watchlist = watchlistService.findById(watchlistId);
        return new ResponseEntity<>(watchlist, HttpStatus.FOUND);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(@RequestHeader("Authorization") String jwt,@PathVariable String coinId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        watchlistService.addCoinToWatchlist(coin,user);
        return new ResponseEntity<>(coin,HttpStatus.OK);
    }




}
