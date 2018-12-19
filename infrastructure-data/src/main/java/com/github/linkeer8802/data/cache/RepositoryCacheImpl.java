package com.github.linkeer8802.data.cache;

import com.github.linkeer8802.data.cache.annotation.CacheConfig;
import com.github.linkeer8802.data.cache.annotation.CacheType;
import com.github.linkeer8802.data.cache.annotation.Cacheable;
import com.github.linkeer8802.data.entity.AbstractEntity;
import com.github.linkeer8802.data.entity.Page;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: weird
 * @date: 2018/12/13
 */
public class RepositoryCacheImpl<T extends AbstractEntity<ID>, ID extends Serializable> implements RepositoryCache {

    static final String CACHE_PREFIX = "CACHE_";

    private KeyGenerator keyGenerator;
    private RedisTemplate redisTemplate;

    public RepositoryCacheImpl(RedisTemplate redisTemplate, KeyGenerator keyGenerator) {
        this.redisTemplate = redisTemplate;
        this.keyGenerator = keyGenerator;
    }

    @Override
    /**
     * 缓存查询方法的值（null值也需要缓存，防止查询不存在的值时频繁请求数据库）
     */
    public Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable {

        Cacheable cacheable = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Cacheable.class);

        String key = getKey(joinPoint, cacheable);
        String cacheName = getCacheName(joinPoint);
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
        //cacheValue有两种类型，分别是T和ID索引列表
        Object cacheValue = opsForHash.get(cacheName, key);

        if (cacheable.cacheType().equals(CacheType.ENTITY)) {
            return cacheableEntities(joinPoint, cacheValue, cacheName, key);
        } else {
            return cacheableValue(joinPoint, cacheValue, cacheName, key);
        }
    }

    private Object cacheableEntities(ProceedingJoinPoint joinPoint, Object value, String cacheName, String key) throws Throwable {
        //从缓存中读取
        if (value != null) {
            if (value instanceof AbstractEntity) {
                return value;
            } else {
                Collection<ID> ids = (Collection<ID>) value;
                if (ids.isEmpty()) {
                    return null;
                } else {
                    //根据ID索引查询出T
                    HashOperations<String, ID, T> entityOpsForHash = redisTemplate.opsForHash();
                    Collection<T> entities = entityOpsForHash.multiGet(cacheName, ids);

                    if (AbstractEntity.class.isAssignableFrom(getReturnType(joinPoint))) {
                        return entities.stream().findFirst().get();
                    } else {
                        return entities;
                    }
                }
            }
        } else {
            try {

                Object result = joinPoint.proceed(joinPoint.getArgs());

                Collection<ID> ids = new ArrayList<>();
                Map<ID, T> entities = new HashMap<>(10);

                if (result == null) {
                    ids = Collections.emptyList();
                    entities = Collections.emptyMap();
                } else if (result instanceof AbstractEntity) {
                    ID id = ((T) result).getId();
                    ids.add(id);
                    entities.put(id, (T) result);
                } else if (result instanceof Collection) {
                    Collection<T> collection = (Collection<T>)(result);
                    Collection<ID> values = collection.stream().map(
                            item -> (item).getId()).collect(Collectors.toList());
                    ids.addAll(values);
                    for (T entity : collection) {
                        entities.put(entity.getId(), entity);
                    }
                } else if (result instanceof Page) {
                    Collection<T> collection = ((Page<T>)result).getContent();
                    Collection<ID> values = collection.stream().map(
                            item -> (item).getId()).collect(Collectors.toList());
                    ids.addAll(values);
                    for (T entity : collection) {
                        entities.put(entity.getId(), entity);
                    }
                } else {
                    throw new IllegalStateException("@Cacheable annotation use error, " +
                            "return value must be AbstractEntity instance or AbstractEntity instance collection.");
                }

                //缓存索引
                cacheIndexs(key, cacheName, ids);
                //缓存实体
                cacheEntities(cacheName, entities);

                return result;

            } catch (Throwable e) {
                throw e;
            }
        }
    }

    private Object cacheableValue(ProceedingJoinPoint joinPoint, Object value, String cacheName, String key) throws Throwable {
        if (value != null) {
            return value;
        } else {
            try {
                value = joinPoint.proceed(joinPoint.getArgs());
                //缓存原始类型值
                redisTemplate.opsForHash().put(cacheName, key, value);

                return value;

            } catch (Throwable e) {
                throw e;
            }
        }
    }

    @Override
    public Object cachePut(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            String cacheName = getCacheName(joinPoint);

            result = joinPoint.proceed(joinPoint.getArgs());

            Map<ID, T> entities = new HashMap<>(10);

            if (result instanceof AbstractEntity) {
                ID id = ((T) result).getId();
                entities.put(id, (T) result);
            } else if (result instanceof Collection) {
                Collection<T> collection = (Collection<T>)(result);
                for (T entity : collection) {
                    entities.put(entity.getId(), entity);
                }
            } else {
                throw new IllegalStateException("@CachePut annotation use error, " +
                        "return value must be AbstractEntity instance or AbstractEntity instance collection.");
            }

            cacheEntities(cacheName, entities);

        } catch (Throwable t) {
            throw t;
        }
        return result;
    }

    @Override
    public Object cacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        String cacheName = getCacheName(joinPoint);
        Object arg = joinPoint.getArgs()[0];
        List<ID> ids = new ArrayList<>();

        if (arg instanceof AbstractEntity) {
            ids.add(((T) arg).getId());
        } else if (arg instanceof Collection) {
            List<ID> values = (List<ID>) ((Collection) arg).stream()
                    .map(item -> ((T)item).getId()).collect(Collectors.toList());
            ids.addAll(values);
        } else if (arg instanceof Serializable) {
            ids.add((ID) arg);
        }

        HashOperations<String, ID, T> entityOpsForHash = redisTemplate.opsForHash();
        entityOpsForHash.delete(cacheName, ids.toArray());

        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable t) {
            throw t;
        }

        return result;
    }

    @Override
    public Object cacheEvictAll(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            redisTemplate.delete(getCacheName(joinPoint));
            result = joinPoint.proceed(joinPoint.getArgs());
        }catch (Throwable t) {
            throw t;
        }

        return result;
    }

    /**
     * 缓存索引
     * @param key
     * @param cacheName
     * @param ids
     */
    private void cacheIndexs(String key, String cacheName, Collection<ID> ids) {
        HashOperations<String, String, Collection<ID>> indexOpsForHash = redisTemplate.opsForHash();
        indexOpsForHash.put(cacheName, key, ids);
    }

    /**
     * 缓存实体
     * @param cacheName
     * @param entities
     */
    private void cacheEntities(String cacheName, Map<ID, T> entities) {
        HashOperations<String, ID, T> entityOpsForHash = redisTemplate.opsForHash();
        for (Map.Entry<ID, T> entry : entities.entrySet()) {
            entityOpsForHash.put(cacheName, entry.getKey(), entry.getValue());
        }
    }

    private Class<?> getReturnType(ProceedingJoinPoint joinPoint) {
        return ((MethodSignature)joinPoint.getSignature()).getMethod().getReturnType();
    }

    private String getKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        if (cacheable != null) {
            if (StringUtils.isNotBlank(cacheable.key())) {
                return cacheable.key();

            } else if (cacheable.firstArgValueAsKey()
                    && joinPoint.getArgs().length == 1
                    && !(joinPoint.getArgs()[0].getClass().isArray())) {

                return joinPoint.getArgs()[0].toString();
            }
        }

        return keyGenerator.generate(joinPoint.getTarget(),
                ((MethodSignature)joinPoint.getSignature()).getMethod(), joinPoint.getArgs()).toString();
    }

    private String getCacheName(ProceedingJoinPoint joinPoint) {
        CacheConfig cacheConfig = joinPoint.getTarget().getClass().getAnnotation(CacheConfig.class);
        if (cacheConfig != null) {
            return CACHE_PREFIX + cacheConfig.value().toUpperCase() + ":";
        } else {
            return CACHE_PREFIX + joinPoint.getTarget().getClass().getSimpleName().toUpperCase() + ":";
        }
    }
}
