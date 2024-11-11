package com.ecommerce.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate; // Keep the original template type

    public <T> Optional<T> get(Long key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key.toString());
            if (o != null) {
                ObjectMapper mapper = new ObjectMapper();
                return Optional.of(mapper.convertValue(o, entityClass));
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public <T> void set(Long key, Object o, int ttl) {
        try {
            redisTemplate.opsForValue().set(key.toString(), o, ttl, TimeUnit.DAYS);
        } catch (Exception e) {
        }
    }
}
