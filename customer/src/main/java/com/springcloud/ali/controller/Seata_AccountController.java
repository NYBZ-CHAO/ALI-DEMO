package com.springcloud.ali.controller;

import com.springcloud.ali.entity.Account;
import com.springcloud.ali.entity.CommonResult;
import com.springcloud.ali.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class Seata_AccountController {



    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/account/decrease")
    public CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money) {
        accountService.decrease(userId, money);
        return new CommonResult(200, "扣减账户余额成功");
    }

    @PostMapping("/account/addAccount_post")
    public CommonResult addAccount_post(){
        Account account = Account.builder().userId(2L).total(8000).used(100).residue(7900).build();
        boolean save = accountService.save(account);
        return new CommonResult(200, "success");
    }


}
