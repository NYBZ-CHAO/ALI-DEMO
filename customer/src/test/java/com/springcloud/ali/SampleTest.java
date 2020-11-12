package com.springcloud.ali;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {


    @Test
    public void beanTest() {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                15,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(20));

        threadPoolExecutor.execute(() -> {

        });

    }

}
