package com.project.smart_wallet.controller.openapi;

import com.project.smart_wallet.dto.request.CreateAssetRequest;
import com.project.smart_wallet.dto.response.AssetSummaryResponse;
import com.project.smart_wallet.dto.response.CreateAssetResponse;
import com.project.smart_wallet.dto.response.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;

@Tag(name = "Assets", description = "Endpoints for managing assets")
public interface AssetControlerOpenApi {

    @Operation(
            summary = "Create an asset",
            description = "<p>Registers an asset with its name, symbol and type.</p>"
    )
    CreateAssetResponse createAsset(CreateAssetRequest request);

    @Operation(
            summary = "List assets",
            description = "<p>Returns registered assets with pagination and sorting.</p>"
    )
    PaginatedResponse<AssetSummaryResponse> listAssets(@ParameterObject Pageable pageable);
}
