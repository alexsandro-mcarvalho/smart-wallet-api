package com.project.smart_wallet.config.redis;

import com.project.smart_wallet.cache.dto.AssetPriceCacheEntry;
import com.project.smart_wallet.cache.dto.IdempotencyCacheEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, AssetPriceCacheEntry> assetPriceRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, AssetPriceCacheEntry> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(AssetPriceCacheEntry.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, IdempotencyCacheEntry> idempotencyTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, IdempotencyCacheEntry> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(IdempotencyCacheEntry.class));
        return template;
    }
}
