package com.ali.rocket.service;


import com.ali.rocket.entity.Account;
import com.ali.rocket.entity.AccountChangeEvent;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AccountInfoService extends IService<Account> {

    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    void addDefaultAccount();

}
