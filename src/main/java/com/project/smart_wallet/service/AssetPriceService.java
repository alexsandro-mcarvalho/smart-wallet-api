package com.project.smart_wallet.service;

import com.project.smart_wallet.cache.AssetPriceCache;
import com.project.smart_wallet.client.AssetPriceProvider;
import com.project.smart_wallet.client.dto.AssetPriceLookUp;
import com.project.smart_wallet.domain.Asset;
import com.project.smart_wallet.domain.AssetType;
import com.project.smart_wallet.cache.dto.AssetPriceCacheEntry;
import com.project.smart_wallet.client.mapper.AssetPriceLookupMapper;
import com.project.smart_wallet.repository.AssetRepository;
import com.project.smart_wallet.utils.BatchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Collectors;

import static com.project.smart_wallet.cache.key.AssetPriceCacheKey.buildKey;
import static java.util.concurrent.StructuredTaskScope.*;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;

@Service
@RequiredArgsConstructor
public class AssetPriceService {

    private final List<AssetPriceProvider> assetPriceProviders;

    private final AssetRepository assetRepository;

    private final RedisTemplate<String, AssetPriceCacheEntry> redisTemplate;

    private final AssetPriceCache assetPriceCache;

    private final Clock clock;

    public void refreshPrices(AssetType assetType) {
        List<Asset> assets = assetRepository.findAllByAssetType(assetType);
        if (assets.isEmpty()) { return; }

        List<AssetPriceLookUp> assetPriceLookUps = assets.stream()
                .map(AssetPriceLookupMapper::toResponse)
                .toList();

        Map<String, BigDecimal> assetsPrices = getAssetsPrice(assetPriceLookUps, assetType);
        Instant updatedAt = Instant.now(clock);

        Map<String, AssetPriceCacheEntry> cacheValues =  assetsPrices.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> buildKey(entry.getKey(), assetType),
                        entry -> new AssetPriceCacheEntry(entry.getValue(), updatedAt)
                ));

        assetPriceCache.saveAll(cacheValues);
    }


    private Map<String, BigDecimal> getAssetsPrice(List<AssetPriceLookUp> assetPriceLookUps, AssetType assetType)  {
        AssetPriceProvider provider = getProvider(assetType);

        List<List<AssetPriceLookUp>> assetsBatches = BatchUtils.partition(
                assetPriceLookUps,
                provider.getQuantityPerRequest()
        );

        try (var scope = StructuredTaskScope.open(Joiner.awaitAll())) {

            List<Subtask<Map<String, BigDecimal>>> subtasks = new ArrayList<>();

            for (var batches : assetsBatches) {
                var subtask = scope.fork(() -> provider.getPricePerAsset(batches));
                subtasks.add(subtask);
            }

            try {
                scope.join();

                Map<String, BigDecimal> prices = new HashMap<>();

                for (var subtask : subtasks) {
                    if (subtask.state() == SUCCESS) {
                        prices.putAll(subtask.get());
                    }
                }

                return prices;
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }

    }

    private AssetPriceProvider getProvider(AssetType assetType) {
        return assetPriceProviders.stream()
                .filter(provider -> provider.getAssetType() == assetType)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma provider encontrado"));
    }

}
