//package com.github.linkeer8802.infrastructure.data.example.config.aop;
//
//import com.github.linkeer8802.data.datasource.DataSourceAspectSupport;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * @author: weird
// * @date: 2018/12/5
// */
//@Component
//@Aspect
//public class DataSourceAspect extends DataSourceAspectSupport {
//
//    @Pointcut("execution(public * com.github.linkeer8802.infrastructure.data.example.service..*.*(..))")
//    public void dataSourceBefore(){}
//
//    @Override
//    @Before("dataSourceBefore()")
//    public void doDataSourceBefore(JoinPoint joinPoint) {
//        super.doDataSourceBefore(joinPoint);
//    }
//}
