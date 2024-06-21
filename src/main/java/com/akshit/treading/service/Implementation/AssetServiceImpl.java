package com.akshit.treading.service.Implementation;

import com.akshit.treading.modal.Asset;
import com.akshit.treading.modal.Coin;
import com.akshit.treading.modal.User;
import com.akshit.treading.repository.AssetRepository;
import com.akshit.treading.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        Optional<Asset> opt = assetRepository.findById(assetId);
        if(opt.isEmpty()) throw new Exception("Asset Not Exist");
        return opt.get();
    }

    @Override
    public Asset getAssetByUserId(Long userId, Long assetId) throws Exception {
        return null;
    }

    @Override
    public List<Asset> getAllAsset(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long AssetId, double quantity) throws Exception {
        Asset asset = getAssetById(AssetId);
        asset.setQuantity(asset.getQuantity()+quantity);
        return assetRepository.save(asset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long AssetId) {
        assetRepository.deleteById(AssetId);
    }
}
