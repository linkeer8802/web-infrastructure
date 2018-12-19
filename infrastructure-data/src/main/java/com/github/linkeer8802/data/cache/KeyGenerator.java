package com.github.linkeer8802.data.cache;

import java.lang.reflect.Method;

/**
 * @author: weird
 * @date: 2018/12/15
 */
public interface KeyGenerator {

    Object generate(Object target, Method method, Object... params);
}
