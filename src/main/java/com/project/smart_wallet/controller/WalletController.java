package com.project.smart_wallet.controller;

import com.project.smart_wallet.controller.openapi.WalletControlerOpenApi;
import com.project.smart_wallet.dto.response.BalanceResponse;
import com.project.smart_wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController implements WalletControlerOpenApi {

    private final WalletService walletService;

    @Override
    @GetMapping
    public BalanceResponse getBalance() {
        return walletService.getBalance();
    }
}
