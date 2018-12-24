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
public class RedisRepoCacheImpl<T extends AbstractEntity<ID>, ID extends Serializable> implements RepositoryCache {

    static final String CACHE_PREFIX = "CACHE_";

    private KeyGenerator keyGenerator;
    private RedisTemplate redisTemplate;

    public RedisRepoCacheImpl(RedisTemplate redisTemplate, KeyGenerator keyGenerator) {
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
        String cacheBaseName = getCacheBaseName(joinPoint);
        HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();

        if (cacheable.cacheType().equals(CacheType.ENTITY)) {
            //cacheValue有两种类型，分别是T和Collection<ID>索引集合
            Object cacheValue = opsForHash.get(entityCacheName(cacheBaseName), key);
            return cacheableEntities(joinPoint, cacheValue, cacheBaseName, key);
        } else {
            Object cacheValue = opsForHash.get(valueIndexCacheName(cacheBaseName), key);
            return cacheableValue(joinPoint, cacheValue, cacheBaseName, key);
        }
    }

    private Object cacheableEntities(ProceedingJoinPoint joinPoint, Object value, String cacheBaseName, String key) throws Throwable {
        //命中缓存
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
                    Collection<T> entities = entityOpsForHash.multiGet(valueIndexCacheName(cacheBaseName), ids);

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
                    throw new IllegalStateException("@Cacheable annotation usage error, " +
                            "return value must be AbstractEntity instance or AbstractEntity instance collection.");
                }

                //缓存索引
                cacheIndexs(key, cacheBaseName, ids);
                //缓存实体
                cacheEntities(cacheBaseName, entities);

                return result;

            } catch (Throwable e) {
                throw e;
            }
        }
    }

    private Object cacheableValue(ProceedingJoinPoint joinPoint, Object value, String cacheBaseName, String key) throws Throwable {
        if (value != null) {
            return value;
        } else {
            try {
                value = joinPoint.proceed(joinPoint.getArgs());
                //缓存原始类型值
                redisTemplate.opsForHash().put(valueIndexCacheName(cacheBaseName), key, value);

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
            String cacheBaseName = getCacheBaseName(joinPoint);

            result = joinPoint.proceed(joinPoint.getArgs());

            //擦除所有值和索引的缓存
            cacheEvictAllValueAndIndex(cacheBaseName);

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
                throw new IllegalStateException("@CachePut annotation usage error, " +
                        "return value must be AbstractEntity instance or AbstractEntity instance collection.");
            }

            cacheEntities(cacheBaseName, entities);

        } catch (Throwable t) {
            throw t;
        }
        return result;
    }

    @Override
    public Object cacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        String cacheBaseName = getCacheBaseName(joinPoint);
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
        entityOpsForHash.delete(entityCacheName(cacheBaseName), ids.toArray());

        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            //擦除所有值和索引的缓存
            cacheEvictAllValueAndIndex(cacheBaseName);
        } catch (Throwable t) {
            throw t;
        }

        return result;
    }

    @Override
    public Object cacheEvictAll(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            String cacheBaseName = getCacheBaseName(joinPoint);
            redisTemplate.delete(entityCacheName(cacheBaseName));
            redisTemplate.delete(valueIndexCacheName(cacheBaseName));
            result = joinPoint.proceed(joinPoint.getArgs());
        }catch (Throwable t) {
            throw t;
        }

        return result;
    }

    /**
     * 对实体执行写操作后，擦除所有值和索引的缓存
     * @param cacheBaseName
     */
    private void cacheEvictAllValueAndIndex(String cacheBaseName) {
        redisTemplate.delete(valueIndexCacheName(cacheBaseName));
    }

    /**
     * 缓存索引
     * @param key
     * @param cacheName
     * @param ids
     */
    private void cacheIndexs(String key, String cacheName, Collection<ID> ids) {
        HashOperations<String, String, Collection<ID>> indexOpsForHash = redisTemplate.opsForHash();
        indexOpsForHash.put(valueIndexCacheName(cacheName), key, ids);
    }

    /**
     * 缓存实体
     * @param cacheName
     * @param entities
     */
    private void cacheEntities(String cacheName, Map<ID, T> entities) {
        HashOperations<String, ID, T> entityOpsForHash = redisTemplate.opsForHash();
        for (Map.Entry<ID, T> entry : entities.entrySet()) {
            entityOpsForHash.put(entityCacheName(cacheName), entry.getKey(), entry.getValue());
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

    private String getCacheBaseName(ProceedingJoinPoint joinPoint) {
        CacheConfig cacheConfig = joinPoint.getTarget().getClass().getAnnotation(CacheConfig.class);
        if (cacheConfig != null) {
            return CACHE_PREFIX + cacheConfig.value().toUpperCase() + ":";
        } else {
            return CACHE_PREFIX + joinPoint.getTarget().getClass().getSimpleName().toUpperCase() + ":";
        }
    }

    private String entityCacheName(String cacheName) {
        return cacheName + "ENTITIES";
    }

    private String valueIndexCacheName(String cacheName) {
        return cacheName + "VALUE_INDEXS";
    }
}
