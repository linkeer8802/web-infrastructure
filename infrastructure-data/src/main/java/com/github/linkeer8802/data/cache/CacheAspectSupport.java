package com.github.linkeer8802.data.cache;

import com.github.linkeer8802.data.cache.annotation.CacheEvict;
import com.github.linkeer8802.data.cache.annotation.CacheEvictAll;
import com.github.linkeer8802.data.cache.annotation.CachePut;
import com.github.linkeer8802.data.cache.annotation.Cacheable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author: weird
 * @date: 2018/12/5
 */
public class CacheAspectSupport {

    @Resource
    RepositoryCache repositoryCache;

    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Annotation[] annotations = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Cacheable) {
                return repositoryCache.cacheable(joinPoint);
            } else if (annotation instanceof CacheEvict) {
                return repositoryCache.cacheEvict(joinPoint);
            } else if (annotation instanceof CacheEvictAll) {
                return repositoryCache.cacheEvictAll(joinPoint);
            } else if (annotation instanceof CachePut) {
                return repositoryCache.cachePut(joinPoint);
            }
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }
}
