package com.project.smart_wallet.domain;

import com.project.smart_wallet.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.project.smart_wallet.testDataBuilder.domain.HoldingBuilder.aHolding;
import static com.project.smart_wallet.testDataBuilder.domain.TransactionBuilder.aBuy;
import static com.project.smart_wallet.testDataBuilder.domain.TransactionBuilder.aSell;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HoldingTest {

    @DisplayName("Should throw BusinessException when initializing a holding with a sell transaction")
    @Test
    void shouldThrowWhenHoldingInitializedWithSellTransaction() {
        Transaction transaction = aSell().build();

        assertThrows(BusinessException.class, () -> new Holding(transaction));
    }

    @DisplayName("Should create a holding when initialized with a buy transaction")
    @Test
    void shouldCreateHoldingWhenInitializedWithBuyTransaction() {
        Holding holding = new Holding(aBuy().build());

        assertNotNull(holding);
    }

    @DisplayName("Should throw a BusinessException when selling more assets than owned")
    @Test
    void shouldThrowWhenSellingMoreAssetsThanOwned() {
        Holding holding = aHolding()
                .withQuantity(new BigDecimal("10"))
                .build();

        Transaction transaction = aSell()
                .withQuantity(new BigDecimal("15"))
                .build();

        assertThrows(BusinessException.class, () -> holding.applyTransaction(transaction));
        assertEquals(new BigDecimal("10"), holding.getQuantity());
    }

    @DisplayName("Should update holding quantity when selling an asset")
    @Test
    void shouldUpdateHoldingQuantityWhenSellingAsset() {
        Holding holding = aHolding()
                .withQuantity(new BigDecimal("15"))
                .build();

        Transaction transaction = aSell()
                .withQuantity(new BigDecimal("5"))
                .build();

        holding.applyTransaction(transaction);

        assertEquals(new BigDecimal("10"), holding.getQuantity());
    }

    @DisplayName("Should update holding quantity and average price when buying an asset")
    @Test
    void shouldUpdateHoldingWhenBuyingAsset() {
        Holding holding = aHolding()
                .withPrice(new BigDecimal("10000"))
                .withQuantity(new BigDecimal("10"))
                .build();

        Transaction transaction = aBuy()
                .withPrice(new BigDecimal("20000"))
                .withQuantity(new BigDecimal("10"))
                .build();

        holding.applyTransaction(transaction);

        assertEquals(new BigDecimal("20"), holding.getQuantity());
        assertEquals(new BigDecimal("1500.00000000"), holding.getAveragePrice());
    }

    @DisplayName("Should set averagePrice to zero when selling all assets")
    @Test
    void ShouldSetAveragePriceToZeroWhenSellingAllAssets() {
        Holding holding = aHolding()
                .withQuantity(new BigDecimal("5"))
                .build();

        Transaction transaction = aSell()
                .withQuantity(new BigDecimal("5"))
                .build();

        holding.applyTransaction(transaction);

        assertEquals(BigDecimal.ZERO, holding.getAveragePrice());
        assertEquals(BigDecimal.ZERO, holding.getQuantity());
    }
}