package com.ali.rocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountChangeEvent implements Serializable {

    private static final long serialVersionUID = -4500554046708018613L;

    private String accountNo;

    private Integer amount;

    private String txNo;
}
