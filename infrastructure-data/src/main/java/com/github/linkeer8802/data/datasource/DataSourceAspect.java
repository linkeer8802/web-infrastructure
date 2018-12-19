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
@Component
@Aspect
public class DataSourceAspect implements Ordered {

    @Pointcut("execution(public * com.example.jdbc.service..*.*(..))")
    public void dataSourceBefore(){}

    @Before("dataSourceBefore()")
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
