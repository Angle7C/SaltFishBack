package com.application.utils;

import cn.hutool.core.lang.Assert;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    private static RedisTemplate<String,Object> redisTemplate;
    @Autowired
    Context context;
    @PostConstruct
    public void init(){
        redisTemplate=context.getRedisTemplate();
        System.out.println(redisTemplate);
    }
    public static void addSet(String key,String token){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(key,token);
    }
    public static boolean existsSetInValue(String key,String token){
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(key,token);
    }
    public static Object removeSetInValue(String key,String token){
        SetOperations setOperations=redisTemplate.opsForSet();
        return setOperations.remove(key,token);
    }
    public static void addSet(String key,String value,Long timeout){
        redisTemplate.expire(key,timeout,TimeUnit.MINUTES);
        SetOperations setOperations=redisTemplate.opsForSet();
        setOperations.add(key,value);
    }
}
