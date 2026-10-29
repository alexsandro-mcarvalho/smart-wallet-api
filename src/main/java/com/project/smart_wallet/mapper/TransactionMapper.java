package com.project.smart_wallet.mapper;

import com.project.smart_wallet.domain.Transaction;
import com.project.smart_wallet.dto.response.TransactionResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionMapper {

    public static TransactionResponse toResponse(Transaction entity) {
        return new TransactionResponse(
                entity.getId(),
                AssetSummaryMapper.toResponse(entity.getAsset()),
                entity.getType(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getTransactionAt()
        );
    }
}
