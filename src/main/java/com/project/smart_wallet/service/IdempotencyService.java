package com.project.smart_wallet.service;

import com.project.smart_wallet.cache.IdempotencyCache;
import com.project.smart_wallet.domain.IdempotencyStatus;
import com.project.smart_wallet.cache.dto.IdempotencyCacheEntry;
import com.project.smart_wallet.conf.properties.IdempotencyProperties;
import com.project.smart_wallet.cache.key.IdempotencyCacheKey;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyProperties idempotencyProperties;

    private final IdempotencyCache idempotencyCache;

    private final UserService userService;

    public boolean markAsProcessing(String redisKey, String payloadHash) {
        return idempotencyCache.saveIfAbsent(
                redisKey,
                new IdempotencyCacheEntry(payloadHash),
                idempotencyProperties.processingTtl()
        );
    }

    public void markAsCompleted(String redisKey, HttpStatusCode statusCode, String body) {
        IdempotencyCacheEntry idempotencyCacheEntry = idempotencyCache.get(redisKey);
        idempotencyCacheEntry.setStatusCode(statusCode.value());
        idempotencyCacheEntry.setIdempotencyStatus(IdempotencyStatus.COMPLETED);
        idempotencyCacheEntry.setResponse(body);

        idempotencyCache.save(redisKey, idempotencyCacheEntry, idempotencyProperties.completedTtl());
    }

    public void markAsFailed(String redisKey) {
        idempotencyCache.delete(redisKey);
    }

    public IdempotencyCacheEntry getIdempotencyCache(String redisKey) {
        return idempotencyCache.get(redisKey);
    }

    public String generatePayloadHash(byte[] body) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(body);

        return HexFormat.of().formatHex(encodedHash);
    }

    public String buildKey(HttpServletRequest request, String idempotencyKey) {
        String userId = userService.getAuthenticatedUserId();
        return IdempotencyCacheKey.buildKey(
                request.getMethod(),
                request.getServletPath(),
                userId,
                idempotencyKey
        );
    }
}
