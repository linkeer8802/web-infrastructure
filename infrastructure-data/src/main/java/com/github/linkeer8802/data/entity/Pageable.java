package com.github.linkeer8802.data.entity;


import java.util.Objects;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public interface Pageable {

    int getPageNumber();

    int getPageSize();

    long getOffset();

    Sort getSort();

    default Sort getSortOr(Sort sort) {

        Objects.requireNonNull(sort, "Fallback Sort must not be null!");

        return getSort().isSorted() ? getSort() : sort;
    }

    boolean hasPrevious();
}
