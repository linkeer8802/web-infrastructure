package com.github.linkeer8802.data.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author: weird
 * @date: 2018/12/5
 */
public class DataSourceAspectSupport implements Ordered {

    public void doDataSourceBefore(JoinPoint joinPoint) {
        DataSource datasource = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(DataSource.class);
        if (datasource != null) {
            DynamicDataSource.DataSourceHolder.setDataSource(datasource.value());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
