package com.project.smart_wallet.interceptor;

import com.project.smart_wallet.domain.IdempotencyStatus;
import com.project.smart_wallet.cache.dto.IdempotencyCacheEntry;
import com.project.smart_wallet.exception.ConflictException;
import com.project.smart_wallet.exception.PreconditionFailedException;
import com.project.smart_wallet.service.IdempotencyService;
import com.project.smart_wallet.annotation.Idempotent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Configuration
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final IdempotencyService idempotencyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {

            Method method = handlerMethod.getMethod();

            if (!method.isAnnotationPresent(Idempotent.class)) {
                return true;
            }

            String idempotencyKey = request.getHeader("Idempotency-Key");

            if (idempotencyKey == null || idempotencyKey.isBlank()) {
                throw new PreconditionFailedException("Idempotency-Key não informado.");
            }

            String idempotencyCacheKey = idempotencyService.buildKey(request, idempotencyKey);

            byte[] body = request.getInputStream().readAllBytes();
            String payloadHash = idempotencyService.generatePayloadHash(body);

            boolean transactionProcessed = idempotencyService.markAsProcessing(idempotencyCacheKey, payloadHash);

            if (transactionProcessed) {
                return true;
            }

            IdempotencyCacheEntry idempotencyCache = idempotencyService.getIdempotencyCache(idempotencyCacheKey);

            if (!idempotencyCache.getPayloadHash().equals(payloadHash)) {
                throw new ConflictException("Idempotency-Key já utilizada para outra requisição.");
            }

            if (idempotencyCache.getIdempotencyStatus() == IdempotencyStatus.PROCESSING) {
                throw new ConflictException("Já existe uma requisição em processamento para essa Idempotency-Key.");
            }

            String idempotencyResponse = idempotencyCache.getResponse();
            response.setStatus(idempotencyCache.getStatusCode());

            if (idempotencyResponse != null) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(idempotencyCache.getResponse());
            }

            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex
    ) throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            if (method.isAnnotationPresent(Idempotent.class) && ex != null) {
                String idempotencyKey = request.getHeader("Idempotency-Key");
                String idempotencyCacheKey = idempotencyService.buildKey(request, idempotencyKey);

                idempotencyService.markAsFailed(idempotencyCacheKey);
            }
        }
    }
}
