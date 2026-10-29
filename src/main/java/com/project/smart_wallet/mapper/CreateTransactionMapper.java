package com.project.smart_wallet.mapper;

import com.project.smart_wallet.domain.Asset;
import com.project.smart_wallet.domain.Transaction;
import com.project.smart_wallet.domain.User;
import com.project.smart_wallet.dto.request.CreateTransactionRequest;
import com.project.smart_wallet.dto.response.CreateTransactionResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateTransactionMapper {

    public static Transaction toEntity(CreateTransactionRequest request, User user, Asset asset) {
        return new Transaction(
                request.quantity(),
                request.price(),
                request.transactionAt(),
                request.type(),
                user,
                asset
        );
    }

    public static CreateTransactionResponse toResponse(Transaction entity) {
        return new CreateTransactionResponse(
                entity.getId(),
                AssetSummaryMapper.toResponse(entity.getAsset()),
                entity.getType(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getTransactionAt(),
                entity.getCreatedAt()
        );
    }
}
