package com.github.linkeer8802.data.datasource;

import java.lang.annotation.*;

/**
 * @author: weird
 * @date: 2018/12/5
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DataSourceType value();
}
