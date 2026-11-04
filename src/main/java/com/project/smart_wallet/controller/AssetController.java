package com.project.smart_wallet.controller;

import com.project.smart_wallet.controller.openapi.AssetControlerOpenApi;
import com.project.smart_wallet.dto.request.CreateAssetRequest;
import com.project.smart_wallet.dto.response.AssetSummaryResponse;
import com.project.smart_wallet.dto.response.CreateAssetResponse;
import com.project.smart_wallet.dto.response.PaginatedResponse;
import com.project.smart_wallet.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetController implements AssetControlerOpenApi {

    private final AssetService assetService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAssetResponse createAsset(@Valid @RequestBody CreateAssetRequest request) {
        return assetService.createAsset(request);
    }

    @Override
    @GetMapping
    public PaginatedResponse<AssetSummaryResponse> listAssets(
            @PageableDefault(sort = {"name"})
            Pageable pageable
    ) {
        return assetService.listTransactions(pageable);
    }

}
