package com.github.linkeer8802.data.repository;

import com.github.linkeer8802.data.entity.AbstractEntity;

import java.io.Serializable;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public interface Repository<T extends AbstractEntity<ID>, ID extends Serializable> {

}
