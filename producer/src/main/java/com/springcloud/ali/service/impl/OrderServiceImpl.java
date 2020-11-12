package com.springcloud.ali.service.impl;

import com.springcloud.ali.dao.OrderDao;
import com.springcloud.ali.entity.Order;
import com.springcloud.ali.feign.CustomerFeignService;
import com.springcloud.ali.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private CustomerFeignService customerFeignService;

    /**
     * 创建订单->调用账户服务扣减账户余额->修改订单状态
     * GlobalTransactional seata开启分布式事务,异常时回滚,name保证唯一即可
     *
     * 1.D:\seata\seata\conf  执行 db_store.sql && db_undo_log.sql
     * 2.D:\seata\seata\conf  修改 file.conf && registry.conf
     * 3.配置 alibaba.seata.tx-service-group.ali_tx_group
     * 4.复制 file.conf && registry.conf 到 resource
     * 5.注解 @GlobalTransactional
     * @param order
     */
    @Override
    @GlobalTransactional(name = "ali-create-order", rollbackFor = Exception.class) // 发生任何异常都回滚
    public void create(Order order) {
        log.info("----->开始新建订单");
        orderDao.create(order);
        log.info("----->结束新建订单");

        log.info("----->开始捡钱");
        customerFeignService.decrease(order.getUserId(),order.getMoney());
        log.info("----->结束捡钱");

        log.info("----->开始修改订单状态");
        orderDao.update(order.getUserId(),0);
        log.info("----->结束修改订单状态");
    }
}
