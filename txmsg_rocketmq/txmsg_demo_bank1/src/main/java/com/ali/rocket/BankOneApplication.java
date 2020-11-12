package com.ali.rocket;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BankOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankOneApplication.class,args);
    }
}
