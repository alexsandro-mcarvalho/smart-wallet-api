package com.project.smart_wallet.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "idempotency")
public record IdempotencyProperties(Duration processingTtl, Duration completedTtl) {
}
