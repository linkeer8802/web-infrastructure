package com.github.linkeer8802.data.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: weird
 * @date: 2018/12/14
 */
public class Sort implements Serializable {

    private static final Sort UNSORTED = Sort.by(new Order[0]);

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private List<Order> orders;

    public Sort(Order... orders) {
        this(Arrays.asList(orders));
    }

    public Sort(List<Order> orders) {

        Objects.requireNonNull(orders, "Orders must not be null!");

        this.orders = Collections.unmodifiableList(orders);
    }

    public Sort(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    public Sort(Direction direction, String... properties) {
        this(direction, properties == null ? new ArrayList<>() : Arrays.asList(properties));
    }

    public Sort(Direction direction, List<String> properties) {

        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.orders = new ArrayList<>(properties.size());

        for (String property : properties) {
            this.orders.add(new Order(direction, property));
        }
    }

    public static Sort by(String... properties) {

        Objects.requireNonNull(properties, "Properties must not be null!");

        return properties.length == 0 ? Sort.unsorted() : new Sort(properties);
    }

    public static Sort by(List<Order> orders) {

        Objects.requireNonNull(orders, "Orders must not be null!");

        return orders.isEmpty() ? Sort.unsorted() : new Sort(orders);
    }

    public static Sort by(Order... orders) {

        Objects.requireNonNull(orders, "Orders must not be null!");

        return new Sort(orders);
    }

    public static Sort by(Direction direction, String... properties) {

        Objects.requireNonNull(direction, "Direction must not be null!");
        Objects.requireNonNull(properties, "Properties must not be null!");

        return Sort.by(Arrays.stream(properties)
                .map(it -> new Order(direction, it))
                .collect(Collectors.toList()));
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public Sort descending() {
        return withDirection(Direction.DESC);
    }

    public Sort ascending() {
        return withDirection(Direction.ASC);
    }

    public boolean isSorted() {
        return !orders.isEmpty();
    }

    public boolean isUnsorted() {
        return !isSorted();
    }

    public Sort and(Sort sort) {

        Objects.requireNonNull(sort, "Sort must not be null!");

        ArrayList<Order> these = new ArrayList<>(this.orders);

        for (Sort.Order order : sort.orders) {
            these.add(order);
        }

        return Sort.by(these);
    }

    public Order getOrderFor(String property) {

        for (Order order : this.orders) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Sort)) {
            return false;
        }

        Sort that = (Sort) obj;

        return this.orders.equals(that.orders);
    }

    @Override
    public int hashCode() {

        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return orders.isEmpty() ? "UNSORTED" : StringUtils.join(orders, ",");
    }

    private Sort withDirection(Direction direction) {

        return Sort.by(orders.stream().map(it -> new Order(direction, it.getProperty())).collect(Collectors.toList()));
    }

    public static enum Direction {

        ASC, DESC;

        public boolean isAscending() {
            return this.equals(ASC);
        }

        public boolean isDescending() {
            return this.equals(DESC);
        }

        public static Direction of(String value) {

            try {
                return Sort.Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }
    }

    public static class Order implements Serializable {

        private static final boolean DEFAULT_IGNORE_CASE = false;

        private Direction direction;
        private String property;
        private boolean ignoreCase;

        public Order(Direction direction, String property) {
            this(direction, property, DEFAULT_IGNORE_CASE);
        }

        public Order(String property) {
            this(DEFAULT_DIRECTION, property);
        }

        public static Order by(String property) {
            return new Order(DEFAULT_DIRECTION, property);
        }

        public static Order asc(String property) {
            return new Order(Direction.ASC, property);
        }

        public static Order desc(String property) {
            return new Order(Direction.DESC, property);
        }

        private Order(Direction direction, String property, boolean ignoreCase) {

            if (StringUtils.isBlank(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            }

            this.direction = direction == null ? DEFAULT_DIRECTION : direction;
            this.property = property;
            this.ignoreCase = ignoreCase;
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

        public boolean isAscending() {
            return this.direction.isAscending();
        }

        public boolean isDescending() {
            return this.direction.isDescending();
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public Order with(Direction direction) {
            return new Sort.Order(direction, this.property, this.ignoreCase);
        }

        public Order withProperty(String property) {
            return new Order(this.direction, property, this.ignoreCase);
        }

        public Sort withProperties(String... properties) {
            return Sort.by(this.direction, properties);
        }

        public Order ignoreCase() {
            return new Order(direction, property, true);
        }

        @Override
        public int hashCode() {

            int result = 17;

            result = 31 * result + direction.hashCode();
            result = 31 * result + property.hashCode();
            result = 31 * result + (ignoreCase ? 1 : 0);

            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Order)) {
                return false;
            }

            Order that = (Order) obj;

            return this.direction.equals(that.direction)
                    && this.property.equals(that.property)
                    && this.ignoreCase == that.ignoreCase;
        }

        @Override
        public String toString() {

            String result = String.format("%s: %s", property, direction);

            if (ignoreCase) {
                result += ", ignoring case";
            }

            return result;
        }
    }
}
