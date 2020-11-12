package com.ali.rocket.dao;


import com.ali.rocket.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface AccountInfoDao extends BaseMapper<Account> {

    int updateAccountBalance(@Param("accountNo") String accountNo,@Param("amount") Integer amount);

}
