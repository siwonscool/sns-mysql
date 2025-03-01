package com.example.sns.util;

import org.springframework.data.domain.Sort;

import java.util.List;

public class PageHelper {
    public static String orderBy(Sort sort) {
        if (sort.isEmpty()) {
            return "id desc";
        }

        List<Sort.Order> orders = sort.toList();

        var orderBys = orders.stream()
                .map(order -> order.getProperty() + " " + order.getDirection())
                .toList();

        return String.join(", ", orderBys);

    }
}
