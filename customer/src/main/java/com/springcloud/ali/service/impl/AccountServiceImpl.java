package com.springcloud.ali.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.ali.dao.AccountDao;
import com.springcloud.ali.entity.Account;
import com.springcloud.ali.feign.ProducerFeignService;
import com.springcloud.ali.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountDao, Account> implements AccountService {
    @Resource
    private AccountDao accountDao;

    @Autowired
    private ProducerFeignService producerFeignService;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("*******->account-service中扣减账户余额开始");
        // 模拟超时异常,全局事务回滚
//        try {
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        accountDao.decrease(userId, money);
        log.info("*******->account-service中扣减账户余额结束");
    }

    @Override
    public Boolean addAccount(Account account) {
        return accountDao.addAccount(account);
    }

}
