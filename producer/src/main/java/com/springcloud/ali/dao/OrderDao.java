package com.springcloud.ali.dao;

import com.springcloud.ali.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao {

    int create(Order order);

    /**
     * 修改订单状态，0——>1
     * @param userId
     * @param status
     * @return
     */
    int update(@Param("userId") Long userId, @Param("status") Integer status);

}
