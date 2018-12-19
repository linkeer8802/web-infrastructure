package com.github.linkeer8802.data.entity;

import java.io.Serializable;

/**
 * @author: weird
 * @date: 2018/12/13
 */
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
