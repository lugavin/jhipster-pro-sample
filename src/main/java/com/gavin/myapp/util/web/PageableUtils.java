package com.gavin.myapp.util.web;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

public class PageableUtils {

    public static Page toPage(Pageable pageable) {
        Page result = new Page().setCurrent(pageable.getPageNumber() + 1).setSize(pageable.getPageSize());
        List<OrderItem> orderItems = pageable
            .getSort()
            .stream()
            .map(
                order -> {
                    OrderItem item = new OrderItem();
                    item.setAsc(order.isAscending());
                    item.setColumn(order.getProperty());
                    return item;
                }
            )
            .collect(Collectors.toList());
        result.setOrders(orderItems);
        return result;
    }
}
