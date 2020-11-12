package com.springcloud.ali.service;


import com.springcloud.ali.entity.Order;

public interface OrderService {
    /**
     * 创建订单
     * @param order
     */
    void create(Order order);
}