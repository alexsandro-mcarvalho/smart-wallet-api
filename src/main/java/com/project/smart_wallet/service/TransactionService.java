package com.project.smart_wallet.service;

import com.project.smart_wallet.domain.Asset;
import com.project.smart_wallet.domain.Transaction;
import com.project.smart_wallet.domain.User;
import com.project.smart_wallet.domain.Holding;
import com.project.smart_wallet.dto.request.CreateTransactionRequest;
import com.project.smart_wallet.dto.response.CreateTransactionResponse;
import com.project.smart_wallet.dto.response.PaginatedResponse;
import com.project.smart_wallet.dto.response.TransactionResponse;
import com.project.smart_wallet.exception.NotFoundException;
import com.project.smart_wallet.mapper.TransactionMapper;
import com.project.smart_wallet.repository.AssetRepository;
import com.project.smart_wallet.repository.TransactionRepository;
import com.project.smart_wallet.repository.HoldingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.project.smart_wallet.mapper.CreateTransactionMapper.toEntity;
import static com.project.smart_wallet.mapper.CreateTransactionMapper.toResponse;
import static com.project.smart_wallet.mapper.PageMapper.toResponse;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AssetRepository assetRepository;

    private final HoldingRepository holdingRepository;

    private final UserService userService;

    @Transactional
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        User user = userService.getAuthenticatedUser();
        Asset asset = assetRepository.findById(request.assetId())
                .orElseThrow(() -> new NotFoundException("Asset não encontrado"));

        Transaction transaction = toEntity(request, user, asset);
        Holding holding = applyTransactionToHolding(transaction);

        transactionRepository.save(transaction);
        holdingRepository.save(holding);

        return toResponse(transaction);
    }

    private Holding applyTransactionToHolding(Transaction transaction) {
        Optional<Holding> walletHolding = holdingRepository.findByUserAndAsset(
                transaction.getUser(), transaction.getAsset()
        );

        return walletHolding.map(holding -> holding.applyTransaction(transaction))
                .orElseGet(() -> new Holding(transaction));
    }

    public PaginatedResponse<TransactionResponse> listTransactions(Pageable pageable) {
        User user = userService.getAuthenticatedUser();
        Page<TransactionResponse> paginatedTransactions = transactionRepository.findAllByUser(user, pageable)
                .map(TransactionMapper::toResponse);

        return toResponse(paginatedTransactions);
    }
}
