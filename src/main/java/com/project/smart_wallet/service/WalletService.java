package com.project.smart_wallet.service;

import com.project.smart_wallet.cache.AssetPriceCache;
import com.project.smart_wallet.domain.User;
import com.project.smart_wallet.dto.AssetPosition;
import com.project.smart_wallet.cache.dto.AssetPriceCacheEntry;
import com.project.smart_wallet.dto.response.BalanceResponse;
import com.project.smart_wallet.repository.TransactionRepository;
import com.project.smart_wallet.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.project.smart_wallet.cache.key.AssetPriceCacheKey.buildKey;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final TransactionRepository transactionRepository;

    private final HoldingRepository holdingRepository;

    private final UserService userService;

    private final AssetPriceCache assetPriceCache;

    private static final int MONEY_SCALE = 2;

    private static final RoundingMode MONEY_ROUNDING = RoundingMode.HALF_EVEN;

    public BalanceResponse getBalance() {
        User user = userService.getAuthenticatedUser();

        List<AssetPosition> assetsBalance = holdingRepository.getHoldingsByUserId(user.getId());

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalSpending = BigDecimal.ZERO;

        for (AssetPosition assetPosition : assetsBalance) {
            String identifier = switch (assetPosition.assetType()) {
                case CRYPTO_CURRENCY -> assetPosition.assetName();
                case STOCK -> assetPosition.assetSymbol();
            };

            String cacheKey = buildKey(identifier, assetPosition.assetType());
            AssetPriceCacheEntry assetCachedEntry = assetPriceCache.get(cacheKey);

            BigDecimal currentSpending = assetPosition.quantity().multiply(assetPosition.averagePrice());
            totalSpending = totalSpending.add(currentSpending);

            if (assetCachedEntry != null) {
                BigDecimal currentAmount = assetPosition.quantity().multiply(assetCachedEntry.price());
                totalAmount = totalAmount.add(currentAmount);
            } else {
                // provisioriamente adiciona com fallback o valor investido no ativo
                totalAmount = totalAmount.add(currentSpending);
            }
        }

        return new BalanceResponse(
                totalAmount.setScale(MONEY_SCALE, MONEY_ROUNDING),
                totalSpending.setScale(MONEY_SCALE, MONEY_ROUNDING),
                totalAmount.subtract(totalSpending).setScale(MONEY_SCALE, MONEY_ROUNDING)
        );
    }
}
