package com.springcloud.ali.feign.impl;


import com.springcloud.ali.feign.ProducerFeignService;
import org.springframework.stereotype.Component;

@Component
public class ProducerFeignServiceImpl implements ProducerFeignService {

    @Override
    public String getProducer() {
        return "you get error";
    }
}
