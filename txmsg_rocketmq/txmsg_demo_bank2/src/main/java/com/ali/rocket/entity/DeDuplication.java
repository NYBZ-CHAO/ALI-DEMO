package com.ali.rocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeDuplication implements Serializable {


    private static final long serialVersionUID = -7602987234133802155L;

    private Long id;
    private String txNo;
    private LocalDateTime createTime;

}
