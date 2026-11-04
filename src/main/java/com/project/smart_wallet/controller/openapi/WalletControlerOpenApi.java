package com.project.smart_wallet.controller.openapi;

import com.project.smart_wallet.dto.response.BalanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Wallet", description = "Endpoints for managing the wallet")
public interface WalletControlerOpenApi {

    @Operation(
            summary = "Get wallet balance",
            description = "<p>Returns the authenticated user's wallet value, invested amount and profit or loss.</p>"
    )
    BalanceResponse getBalance();
}
