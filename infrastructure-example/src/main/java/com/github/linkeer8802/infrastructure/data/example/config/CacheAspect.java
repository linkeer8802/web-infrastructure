package com.github.linkeer8802.infrastructure.data.example.config;

import com.github.linkeer8802.data.cache.CacheAspectSupport;
import com.github.linkeer8802.data.cache.RepositoryCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: weird
 * @date: 2018/12/5
 */
@Component
@Aspect
public class CacheAspect extends CacheAspectSupport {

    @Resource
    RepositoryCache repositoryCache;

    @Pointcut("execution(public * com.example.jdbc.repository..*.*(..))")
    public void cacheAround(){}

    @Override
    @Around("cacheAround()")
    public Object cacheAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.cacheAround(joinPoint);
    }
}
