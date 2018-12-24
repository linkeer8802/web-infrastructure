package com.github.linkeer8802.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: weird
 * @date: 2018/12/21
 */
public class RedisRateLimit implements RateLimit {

    Logger log = LoggerFactory.getLogger(RedisRateLimit.class);

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public Boolean requestRateLimit(final String key, Long requestTimes, Integer seconds) {
        Integer limit = redisTemplate.opsForValue().get(key);
        if (limit != null && limit >= requestTimes) {
            log.warn("Request too much, request > {} times in {}", requestTimes, seconds);
            return false;
        }
        if (limit == null) {
            List<Object> value = redisTemplate.execute(new SessionCallback<List<Object>>(){
                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.opsForValue().increment(key);
                    operations.expire(key, seconds, TimeUnit.SECONDS);
                    return operations.exec();
                }
            });

        } else {
            redisTemplate.opsForValue().increment(key);
        }
        return true;
    }
}
