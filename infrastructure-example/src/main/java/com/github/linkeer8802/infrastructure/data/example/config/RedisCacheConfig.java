package com.github.linkeer8802.infrastructure.data.example.config;

import com.github.linkeer8802.data.cache.KeyGenerator;
import com.github.linkeer8802.data.cache.RepositoryCache;
import com.github.linkeer8802.data.cache.RedisRepoCacheImpl;
import com.github.linkeer8802.data.cache.SimpleCacheKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author: weird
 * @date: 2018/12/10
 */
@Configuration
public class RedisCacheConfig {

    @Bean
    public KeyGenerator keyGenerator() {
        KeyGenerator keyGenerator = new SimpleCacheKeyGenerator();
        return keyGenerator;
    }

    @Bean
    public RepositoryCache repositoryCache(RedisTemplate redisTemplate) {
        RepositoryCache repositoryCache = new RedisRepoCacheImpl<>(redisTemplate, keyGenerator());
        return repositoryCache;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }
}
