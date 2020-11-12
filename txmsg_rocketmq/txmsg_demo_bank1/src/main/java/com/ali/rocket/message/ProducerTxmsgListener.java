package com.ali.rocket.message;

import com.ali.rocket.dao.DeDuplicationDao;
import com.ali.rocket.entity.AccountChangeEvent;
import com.ali.rocket.entity.DeDuplication;
import com.ali.rocket.service.AccountInfoService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_bank1")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    DeDuplicationDao deDuplicationDao;

    // 事务消息发送成功后的回调方法
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
       try {
           String str = new String((byte[]) message.getPayload());
           JSONObject jsonObject = JSONObject.parseObject(str);
           AccountChangeEvent accountChangeEvent = jsonObject.getObject("accountChange", AccountChangeEvent.class);
           accountInfoService.doUpdateAccountBalance(accountChangeEvent);
           return RocketMQLocalTransactionState.COMMIT;
       }catch (Exception e){
           e.printStackTrace();
           return RocketMQLocalTransactionState.ROLLBACK;
       }
    }

    // mqServer 回查本地事务是否执行
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String str = new String((byte[]) message.getPayload());
        JSONObject jsonObject = JSONObject.parseObject(str);
        AccountChangeEvent accountChangeEvent = jsonObject.getObject("accountChange", AccountChangeEvent.class);

        Integer num = deDuplicationDao.selectCount(new LambdaQueryWrapper<DeDuplication>()
                .eq(DeDuplication::getTxNo, accountChangeEvent.getTxNo()));

        if (num > 0){
            return RocketMQLocalTransactionState.COMMIT;
        }else {
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
