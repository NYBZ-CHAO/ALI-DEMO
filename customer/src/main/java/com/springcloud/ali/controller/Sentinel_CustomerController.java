package com.springcloud.ali.controller;


import com.springcloud.ali.feign.ProducerFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
@Slf4j
public class Sentinel_CustomerController {

    @Autowired
    private ProducerFeignService producerFeignService;

    @GetMapping("/getProducer")
    public String getProducer(){
        return producerFeignService.getProducer();
    }


}
