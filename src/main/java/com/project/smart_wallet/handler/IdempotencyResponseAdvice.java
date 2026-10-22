package com.project.smart_wallet.handler;

import com.project.smart_wallet.service.IdempotencyService;
import com.project.smart_wallet.annotation.Idempotent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

@RestControllerAdvice
@RequiredArgsConstructor
public class IdempotencyResponseAdvice implements ResponseBodyAdvice<Object> {

    private final IdempotencyService idempotencyService;

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(Idempotent.class);
    }

    @Override
    public @Nullable Object beforeBodyWrite(
            @Nullable Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        String idempotencyKey = servletRequest.getHeader("Idempotency-Key");
        String idempotencyCacheKey = idempotencyService.buildKey(servletRequest, idempotencyKey);

        HttpStatusCode statusCode = HttpStatusCode.valueOf(servletResponse.getStatus());

        if (statusCode.is2xxSuccessful()) {
            idempotencyService.markAsCompleted(idempotencyCacheKey, statusCode, objectMapper.writeValueAsString(body));
        } else {
            idempotencyService.markAsFailed(idempotencyCacheKey);
        }

        return body;
    }
}
