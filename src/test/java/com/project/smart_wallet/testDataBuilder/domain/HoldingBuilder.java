package com.project.smart_wallet.testDataBuilder.domain;

import com.project.smart_wallet.domain.Asset;
import com.project.smart_wallet.domain.Holding;
import com.project.smart_wallet.domain.User;

import java.math.BigDecimal;

public class HoldingBuilder {

    private final TransactionBuilder transactionBuilder = TransactionBuilder.aBuy();

    private HoldingBuilder() {}

    public static HoldingBuilder aHolding() {
        return new HoldingBuilder();
    }

    public HoldingBuilder withUser(User user) {
        transactionBuilder.withUser(user);
        return this;
    }

    public HoldingBuilder withAsset(Asset asset) {
        transactionBuilder.withAsset(asset);
        return this;
    }

    public HoldingBuilder withQuantity(BigDecimal quantity) {
        transactionBuilder.withQuantity(quantity);
        return this;
    }

    public HoldingBuilder withPrice(BigDecimal price) {
        transactionBuilder.withPrice(price);
        return this;
    }

    public Holding build() {
        return new Holding(transactionBuilder.build());
    }
}
