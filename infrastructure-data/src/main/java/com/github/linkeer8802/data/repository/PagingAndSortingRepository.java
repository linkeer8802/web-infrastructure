package com.github.linkeer8802.data.repository;

import com.github.linkeer8802.data.entity.AbstractEntity;
import com.github.linkeer8802.data.entity.Page;
import com.github.linkeer8802.data.entity.Pageable;
import com.github.linkeer8802.data.entity.Sort;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public interface PagingAndSortingRepository<T extends AbstractEntity<ID>, ID extends Serializable> extends CrudRepository<T, ID> {

    <C extends Collection<T>> C findAll(Sort sort);

    Page<T> findAll(Pageable pageable);
}
