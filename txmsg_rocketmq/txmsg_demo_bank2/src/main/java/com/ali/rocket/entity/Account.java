package com.ali.rocket.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements Serializable {


    private static final long serialVersionUID = -121690710926424936L;

    private Long id;

    private String accountName;

    private String accountNo;

    private String accountPwd;

    private Integer accountBalance;
}
