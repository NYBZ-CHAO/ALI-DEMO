package com.springcloud.ali.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springcloud.ali.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountDao extends BaseMapper<Account> {
    /**
     * 扣减账户余额
     *
     * @param userId
     * @param money
     * @return
     */
    int decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);

    Boolean addAccount(Account account);
}
