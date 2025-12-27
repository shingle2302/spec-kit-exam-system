package com.exam.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    // Cache for 5 minutes (300,000 milliseconds)
    public void setWithDefaultTimeout(String key, Object value) {
        set(key, value, 300000);
    }
    
    // Cache for 1 hour (3,600,000 milliseconds)
    public void setWithHourTimeout(String key, Object value) {
        set(key, value, 3600000);
    }
    
    // Cache for 24 hours (86,400,000 milliseconds)
    public void setWithDayTimeout(String key, Object value) {
        set(key, value, 86400000);
    }
    
    // Delete multiple keys by pattern
    public void deletePattern(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern + "*"));
    }
    
    // Cache list of entities
    public void setList(String key, Object value) {
        setWithDefaultTimeout(key, value);
    }
    
    // Cache single entity
    public void setEntity(String key, Object value) {
        setWithDefaultTimeout(key, value);
    }
    
    // Cache search results
    public void setSearchResult(String key, Object value) {
        setWithDefaultTimeout(key, value);
    }
}