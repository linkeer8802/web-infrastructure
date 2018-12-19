package com.github.linkeer8802.data.cache.annotation;

import java.lang.annotation.*;

/**
 * @author: weird
 * @date: 2018/12/14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheEvict {
}
