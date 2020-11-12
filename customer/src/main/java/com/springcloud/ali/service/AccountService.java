package com.springcloud.ali.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.springcloud.ali.entity.Account;

import java.math.BigDecimal;


public interface AccountService extends IService<Account> {

    /**
     * 扣钱
     * @param userId 用户id
     * @param money  金额
     * @return
     */
    void decrease(Long userId, BigDecimal money);

    Boolean addAccount(Account account);

}
