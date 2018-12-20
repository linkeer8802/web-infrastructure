package com.github.linkeer8802.infrastructure.data.example.config;

import com.github.linkeer8802.data.datasource.DataSource;
import com.github.linkeer8802.data.datasource.DataSourceAspectSupport;
import com.github.linkeer8802.data.datasource.DynamicDataSource;
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
public class DataSourceAspect extends DataSourceAspectSupport {

    @Pointcut("execution(public * com.example.jdbc.service..*.*(..))")
    public void dataSourceBefore(){}

    @Override
    @Before("dataSourceBefore()")
    public void doDataSourceBefore(JoinPoint joinPoint) {
        super.doDataSourceBefore(joinPoint);
    }
}
