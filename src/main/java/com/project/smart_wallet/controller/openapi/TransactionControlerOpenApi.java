package com.project.smart_wallet.controller.openapi;

import com.project.smart_wallet.dto.request.CreateTransactionRequest;
import com.project.smart_wallet.dto.response.CreateTransactionResponse;
import com.project.smart_wallet.dto.response.PaginatedResponse;
import com.project.smart_wallet.dto.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;

@Tag(name = "Transactions", description = "Endpoints for managing transactions")
public interface TransactionControlerOpenApi {

    @Operation(
            summary = "Create a transaction",
            description = "<p>Records an asset transaction for the authenticated user and updates the corresponding wallet holding.</p>"
    )
    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    @Operation(
            summary = "List transactions",
            description = "<p>Returns the authenticated user's transactions with pagination and sorting.</p>"
    )
    PaginatedResponse<TransactionResponse> listTransactions(@ParameterObject Pageable pageable);
}
