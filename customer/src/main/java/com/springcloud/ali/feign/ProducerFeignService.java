package com.springcloud.ali.feign;


import com.springcloud.ali.feign.impl.ProducerFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "producer-service",fallback = ProducerFeignServiceImpl.class)
public interface ProducerFeignService {

    @GetMapping("/producer/getProducer")
    String getProducer();


}
