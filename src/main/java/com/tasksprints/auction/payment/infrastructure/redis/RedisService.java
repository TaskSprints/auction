package com.tasksprints.auction.payment.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void saveDataWithExpiration(String key, String value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutSeconds));
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
