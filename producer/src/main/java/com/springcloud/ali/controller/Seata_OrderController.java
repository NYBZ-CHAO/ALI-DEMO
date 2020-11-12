package com.springcloud.ali.controller;

import com.springcloud.ali.entity.CommonResult;
import com.springcloud.ali.entity.Order;
import com.springcloud.ali.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Seata_OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     * @param order
     * @return
     */
    @PostMapping("/order/create")
    public CommonResult create(Order order) {
        orderService.create(order);
        return new CommonResult(200, "订单创建成功");
    }
}