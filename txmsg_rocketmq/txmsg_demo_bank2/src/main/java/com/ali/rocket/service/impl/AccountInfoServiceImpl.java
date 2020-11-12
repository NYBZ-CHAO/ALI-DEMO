package com.ali.rocket.service.impl;


import com.ali.rocket.dao.AccountInfoDao;
import com.ali.rocket.dao.DeDuplicationDao;
import com.ali.rocket.entity.Account;
import com.ali.rocket.entity.AccountChangeEvent;
import com.ali.rocket.entity.DeDuplication;
import com.ali.rocket.service.AccountInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoDao, Account> implements AccountInfoService {


    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    DeDuplicationDao deDuplicationDao;


    /**
     * 跟新账户扣减金额
     *
     * @param accountChangeEvent
     */
    @Override
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {

        Integer num = deDuplicationDao.selectCount(
                new LambdaQueryWrapper<DeDuplication>()
                    .eq(DeDuplication::getTxNo,accountChangeEvent.getTxNo()));

        // 幂等判断
        if (num > 0)
            return;

        // 加钱
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount());
        // 记录事务日志 幂等
        DeDuplication deDuplication = new DeDuplication();
        deDuplication.setTxNo(accountChangeEvent.getTxNo());
        deDuplication.setCreateTime(LocalDateTime.now());
        deDuplicationDao.insert(deDuplication);
    }



}
