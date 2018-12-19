package com.github.linkeer8802.data.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * @author: weird
 * @date: 2018/12/18
 */
public abstract class AbstractJdbcRepository {

    @Resource
    protected JdbcTemplate jdbcTemplate;


}
