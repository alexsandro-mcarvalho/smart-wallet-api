package com.project.smart_wallet.controller;

import com.project.smart_wallet.controller.openapi.AuthControlerOpenApi;
import com.project.smart_wallet.dto.request.LoginRequest;
import com.project.smart_wallet.dto.request.RegisterRequest;
import com.project.smart_wallet.dto.response.LoginResponse;
import com.project.smart_wallet.dto.response.RegisterResponse;
import com.project.smart_wallet.dto.response.TokenResponse;
import com.project.smart_wallet.dto.response.UserResponse;
import com.project.smart_wallet.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControlerOpenApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);

        TokenResponse tokenResponse = loginResponse.tokenResponse();

        ResponseCookie cookie = ResponseCookie.from("accessToken", tokenResponse.token())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(tokenResponse.expiresIn())
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse.userResponse());
    }

    @Override
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
       return authService.register(request);
    }
}
