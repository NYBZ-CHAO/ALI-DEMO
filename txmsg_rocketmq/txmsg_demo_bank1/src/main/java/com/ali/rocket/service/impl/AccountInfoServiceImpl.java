package com.ali.rocket.service.impl;


import com.ali.rocket.dao.AccountInfoDao;
import com.ali.rocket.dao.DeDuplicationDao;
import com.ali.rocket.entity.Account;
import com.ali.rocket.entity.AccountChangeEvent;
import com.ali.rocket.entity.DeDuplication;
import com.ali.rocket.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoDao, Account> implements AccountInfoService {


    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    DeDuplicationDao deDuplicationDao;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    /**
     * 向mq发送消息
     *
     * @param accountChangeEvent
     */
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        JSONObject msg = new JSONObject();
        msg.put("accountChange", accountChangeEvent);
        String jsonString = msg.toJSONString();
        Message<String> message = MessageBuilder.withPayload(jsonString).build();

        /**
         * String txProducerGroup, 生产组
         * String destination,  主题
         * Message<?> message,  消息
         * Object arg
         */
        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", "topic_txmsg", message, null);
    }

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

        // 减钱
        int i = accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount() * -1);
        if (i <= 0){
            throw new RuntimeException();
        }
        // 记录事务日志 幂等
        DeDuplication deDuplication = new DeDuplication();
        deDuplication.setTxNo(accountChangeEvent.getTxNo());
        deDuplication.setCreateTime(LocalDateTime.now());
        deDuplicationDao.insert(deDuplication);
    }

    @Override
    public void addDefaultAccount() {
        Account account = new Account();
        account.setAccountBalance(new Integer(1000));
        account.setAccountName("张三");
        account.setAccountNo("12345678");
        account.setAccountPwd("******");
        accountInfoDao.insert(account);
    }


}
