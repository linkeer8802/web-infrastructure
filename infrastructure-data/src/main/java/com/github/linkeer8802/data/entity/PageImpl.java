package com.github.linkeer8802.data.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author weird
 * @date 2018/12/14
 */
public class PageImpl<T extends AbstractEntity> implements Page<T> {

    private long total;
    private Pageable pageable;
    private List<T> content = new ArrayList<>();

    public PageImpl(List<T> content, Pageable pageable, long total) {

        this.pageable = pageable;
        this.content.addAll(content);
        this.total = total;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getNumber() {
        return pageable != null ? pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return pageable != null ? pageable.getPageSize() : 0;
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
