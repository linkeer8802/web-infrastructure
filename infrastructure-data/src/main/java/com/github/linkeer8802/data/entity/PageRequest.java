package com.github.linkeer8802.data.entity;


/**
 * @author: weird
 * @date: 2018/12/17
 */
public class PageRequest implements Pageable {

    private int page;
    private int size;
    private Sort sort;

    public PageRequest(int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }

        this.page = page;
        this.size = size;
    }

    public PageRequest(int page, int size, Sort sort) {

        this(page, size);

        this.sort = sort;
    }

    public PageRequest(int page, int size, Sort.Direction direction, String... properties) {
        this(page, size, Sort.by(direction, properties));
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public long getOffset() {
        return (long) page * (long) size;
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        result = prime * result + page;
        result = prime * result + size;

        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PageRequest other = (PageRequest) obj;
        return this.page == other.page && this.size == other.size;
    }
}
