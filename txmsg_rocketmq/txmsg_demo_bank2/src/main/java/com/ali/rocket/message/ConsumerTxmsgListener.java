package com.ali.rocket.message;

import com.ali.rocket.dao.DeDuplicationDao;
import com.ali.rocket.entity.AccountChangeEvent;
import com.ali.rocket.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_txmsg_bank2",topic = "topic_txmsg")
public class ConsumerTxmsgListener implements RocketMQListener<String> {

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    DeDuplicationDao deDuplicationDao;


    @Override
    public void onMessage(String s) {
        JSONObject jsonObject = JSONObject.parseObject(s);
        AccountChangeEvent accountChangeEvent = jsonObject.getObject("accountChange", AccountChangeEvent.class);
        // 加钱账户
        accountChangeEvent.setAccountNo("1");
        accountInfoService.doUpdateAccountBalance(accountChangeEvent);
    }
}
