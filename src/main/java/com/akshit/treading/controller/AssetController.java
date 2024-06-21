package com.akshit.treading.controller;

import com.akshit.treading.modal.Asset;
import com.akshit.treading.modal.User;
import com.akshit.treading.service.AssetService;
import com.akshit.treading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@RequestHeader("Authorization") String jwt,@PathVariable String coinId) throws Exception {
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        Asset asset = assetService.findAssetByUserIdAndCoinId(userId,coinId);
        return new ResponseEntity<>(asset,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAsset(@RequestHeader("Authorization") String jwt) throws Exception {
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Asset> list = assetService.getAllAsset(userId);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
