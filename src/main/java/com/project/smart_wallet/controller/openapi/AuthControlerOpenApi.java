package com.project.smart_wallet.controller.openapi;

import com.project.smart_wallet.dto.request.LoginRequest;
import com.project.smart_wallet.dto.request.RegisterRequest;
import com.project.smart_wallet.dto.response.RegisterResponse;
import com.project.smart_wallet.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public interface AuthControlerOpenApi {

    @Operation(
            summary = "Authenticate a user",
            description = "<p>Validates the user's credentials, sets the access token in an <code>HttpOnly</code> cookie and returns the user details.</p>"
    )
    ResponseEntity<UserResponse> login(LoginRequest request);

    @Operation(
            summary = "Register a user",
            description = "<p>Creates a user account using the provided registration details.</p>"
    )
    RegisterResponse register(RegisterRequest request);
}
