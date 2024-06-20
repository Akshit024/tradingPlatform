package com.akshit.treading.controller;

import com.akshit.treading.modal.Coin;
import com.akshit.treading.service.CoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coin> coinList = coinService.getCoinList(page);
        return new ResponseEntity<>(coinList, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@RequestParam("days") int days, @PathVariable String coinId) throws Exception {
        String resp = coinService.getMarketChart(coinId,days);
        JsonNode jsonNode = objectMapper.readTree(resp);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String resp = coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(resp);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String resp = coinService.getTop50CoinByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(resp);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/treading")
    public ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
        String resp = coinService.getTreadingCoin();
        JsonNode jsonNode = objectMapper.readTree(resp);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String resp = coinService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(resp);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

}
