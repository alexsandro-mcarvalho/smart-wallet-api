package com.project.smart_wallet.controller;

import com.project.smart_wallet.dto.request.CreateTransactionRequest;
import com.project.smart_wallet.dto.response.CreateTransactionResponse;
import com.project.smart_wallet.dto.response.PaginatedResponse;
import com.project.smart_wallet.dto.response.TransactionResponse;
import com.project.smart_wallet.service.TransactionService;
import com.project.smart_wallet.annotation.Idempotent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Idempotent
    public CreateTransactionResponse createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping
    public PaginatedResponse<TransactionResponse> listTransactions(
            @PageableDefault(size = 3, sort = {"transactionAt"}, direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return transactionService.listTransactions(pageable);
    }
}
