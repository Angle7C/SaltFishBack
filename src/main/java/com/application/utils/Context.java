package com.application.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Data
public class Context {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
}
