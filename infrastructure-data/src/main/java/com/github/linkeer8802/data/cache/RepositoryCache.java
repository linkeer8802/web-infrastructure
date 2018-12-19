package com.github.linkeer8802.data.cache;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author: weird
 * @date: 2018/12/13
 */
public interface RepositoryCache {
    Object cacheable(ProceedingJoinPoint joinPoint) throws Throwable;
    Object cachePut(ProceedingJoinPoint joinPoint) throws Throwable;
    Object cacheEvict(ProceedingJoinPoint joinPoint) throws Throwable;
    Object cacheEvictAll(ProceedingJoinPoint joinPoint) throws Throwable;
}
