package com.ali.rocket.service;


import com.ali.rocket.entity.Account;
import com.ali.rocket.entity.AccountChangeEvent;
import com.baomidou.mybatisplus.extension.service.IService;


public interface AccountInfoService extends IService<Account> {

    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);


}
