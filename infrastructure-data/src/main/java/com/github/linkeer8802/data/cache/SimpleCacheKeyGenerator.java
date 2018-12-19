package com.github.linkeer8802.data.cache;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author: weird
 * @date: 2018/12/15
 */
public class SimpleCacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        int result = 1;

        if (params == null) {
            result = 0;
        } else {
            for (Object param : params) {
                int elementHash = 0;
                if (param.getClass().isArray()) {
                    elementHash = Arrays.deepHashCode((Object[]) param);
                } else {
                    elementHash = param.hashCode();
                }

                result = 31 * result + elementHash;
            }
        }

        return method.getName() + "_" + result;
    }
}
