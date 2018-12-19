package com.github.linkeer8802.data.repository;

import com.github.linkeer8802.data.entity.AbstractEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public interface CrudRepository<T extends AbstractEntity<ID>, ID extends Serializable> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T, U extends Collection<S>> U saveAll(U entities);

    T findById(ID id);

    boolean existsById(ID id);

    <R extends Collection<T>> R findAll();

    <R extends Collection<T>, U extends Collection<ID>> R findAllById(U ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Collection<? extends T> entities);

    void deleteAll();
}
