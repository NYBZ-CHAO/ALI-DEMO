package com.ali.rocket.controller;


import com.ali.rocket.entity.AccountChangeEvent;
import com.ali.rocket.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountInfoController {


    @Autowired
    private AccountInfoService accountInfoService;

    @GetMapping("transfer")
    public String transfer(String accountNo,Integer amount){

        String txNo = UUID.randomUUID().toString();
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(accountNo,amount,txNo);
        accountInfoService.sendUpdateAccountBalance(accountChangeEvent);

        return "success";
    }

    @RequestMapping("addAccount")
    public void addAccount(){
        accountInfoService.addDefaultAccount();
    }
}
