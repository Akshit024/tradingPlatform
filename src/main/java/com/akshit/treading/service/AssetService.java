package com.akshit.treading.service;

import com.akshit.treading.modal.Asset;
import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.User;

import java.util.List;

public interface AssetService {
    Asset createAsset(User user, Coin coin,double quantity);

    Asset getAssetById(Long assetId) throws Exception;

    Asset getAssetByUserId(Long userId,Long assetId) throws Exception;

    List<Asset> getAllAsset(Long userId);

    Asset updateAsset(Long AssetId,double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId,String coinId);

    void deleteAsset(Long AssetId);
}
