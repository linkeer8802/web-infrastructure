package com.github.linkeer8802.data.entity;

import java.util.List;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public interface Page<T extends AbstractEntity> {

    int getTotalPages();

    long getTotalElements();

    int getNumber();

    int getSize();

    List<T> getContent();

    boolean hasContent();

    Sort getSort();

    boolean isFirst();

    boolean isLast();

    boolean hasNext();

    boolean hasPrevious();
}
