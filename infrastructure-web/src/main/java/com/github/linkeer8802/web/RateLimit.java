package com.github.linkeer8802.web;

/**
 * @author: weird
 * @date: 2018/12/24
 */
public interface RateLimit {
    Boolean requestRateLimit(final String key, Long requestTimes, Integer seconds);
}
