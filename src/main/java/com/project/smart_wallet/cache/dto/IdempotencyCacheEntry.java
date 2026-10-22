package com.project.smart_wallet.cache.dto;

import com.project.smart_wallet.domain.IdempotencyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class IdempotencyCacheEntry {

        @Setter
        private IdempotencyStatus idempotencyStatus;

        @Setter
        private Integer statusCode;

        private final String payloadHash;

        @Setter
        private String response;

        public IdempotencyCacheEntry(String payloadHash) {
            this.idempotencyStatus = IdempotencyStatus.PROCESSING;
            this.payloadHash = payloadHash;
        }
}