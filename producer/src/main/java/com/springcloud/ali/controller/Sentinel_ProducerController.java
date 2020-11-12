package com.springcloud.ali.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/producer")
@Slf4j
public class Sentinel_ProducerController {


//    resource : 资源名，限流规则作用对象，一般为请求URI
//    limitApp : 控流针对的调用来源，default则不区分调用来源
//    grade :  限流阈值类型；0表示根据并发量来限流，1表示根据QPS来进行限流
//    count :  限流阈值
//    strategy ： 调用关系限流策略 (0直接 1关联 2链路)
//    controlBehavior ： 限流控制行为（0快速失败 、1warm up 、2排队等候）
//    warmUpPeriodSec: 预热时长 秒
//    maxQueueingTimeMs: 超时时长 毫秒
//    clusterMode ： 是否为集群模式

    //----------------------------------- 流量控制 ------------------------------------------------

   /* {
            "resource":"/producer/helloSentinel",
            "limitApp":"default",
            "grade":1,
            "count":1,
            "strategy":0,
            "controlBehavior":0,
            "clusterMode":false
    }*/

    /**
     * qps 一秒一个
     *
     * @return
     */
    @GetMapping("/helloSentinel")
    public String helloSentinel() {
        return "hello ~~";
    }

  /*  {
        "resource":"/producer/testD",
            "limitApp":"default",
            "grade":0,
            "count":2,
            "strategy":0,
            "controlBehavior":0,
            "clusterMode":false
    }*/

    /**
     * 每次允许2个线程访问
     *
     * @return
     */
    @GetMapping("/testD")
    public String testD() {
        log.info("testD 测试RT");
        return "testD -----";
    }



    /*{
        "resource":"/producer/testE",
            "limitApp":"default",
            "grade":1,
            "count":10,
            "strategy":0,
            "controlBehavior":1,
            "warmUpPeriodSec":5, 预热时长 5 秒
            "clusterMode":false
    }*/

    /**
     * 预热冷启动  war
     *
     * @return
     */
    @GetMapping("/testE")
    public String testE() {
        log.info("testE 测试RT");
        return "testE -----";
    }


    /*{
        "resource":"/producer/testArray",
            "limitApp":"default",
            "grade":1,
            "count":1,
            "strategy":0,
            "controlBehavior":1,
            "maxQueueingTimeMs":5000,
            "clusterMode":false
    }*/

    /**
     * 排队等待  每秒一个排队请求  超时时间 5000 毫秒
     * 匀速排队模式暂时不支持 QPS > 1000 的场景。
     *
     * @return
     */
    @GetMapping("/testArray")
    public String testArray() {
        log.info("testArray 测试RT");
        return "testArray -----";
    }


    //-------------------------------------- 熔断降级 ---------------------------------------------

//    resource	            资源名，即规则的作用对象
//    grade	                熔断策略，0支持慢调用比例/1异常比例/2异常数策略
//    count	                慢调用比例模式下为慢调用临界 RT【毫秒】（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
//    timeWindow	        熔断时长，单位为 s
//    minRequestAmount	    熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
//    statIntervalMs	    统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
//    slowRatioThreshold	慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）


/*    {
        "resource": "/producer/testRT",
        "limitApp": "default",
        "count": 200,
        "timeWindow": 1,
        "grade": 0,
        "minRequestAmount": 5,
        "slowRatioThreshold": 0.5,
        "statIntervalMs": 1000
    }*/

    /**
     * 熔断策略 为 慢调用比例
     * 慢调用最少5个请求，
     * 最长200毫秒超时，
     * 比例阈值 0.5
     * 满足这三各条件 熔断  1s后系统重新启用
     *
     * @return
     */
    @GetMapping("/testRT")
    public String testRT() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testRT 测试RT");
        return "testRT -----";
    }


//    {
//        "resource": "/producer/testError",
//            "limitApp": "default",
//            "count": 0.2,
//            "timeWindow": 3,
//            "grade": 1,
//            "minRequestAmount": 5
//    }

    /**
     * 熔断策略 为 异常比例
     * 最少5个请求，
     * 比例阈值 0.2
     * 满足这两各条件 熔断  3s后系统重新启用
     *
     * @return
     */
    @GetMapping("/testError")
    public String testError() {
        log.info("testError 测试testError");
        int i = 10 / 0;
        return "testError -----";
    }


//    {
//        "resource": "/producer/testErrorNum",
//            "count": 10,
//            "timeWindow": 1,
//            "grade": 2,
//            "minRequestAmount": 20
//    }

    /**
     * 熔断策略 为 异常数
     * 最少5个请求，
     * 异常数 10
     * 满足这两各条件 熔断  1s后系统重新启用
     *
     * @return
     */
    @GetMapping("/testErrorNum")
    public String testErrorNum() {
        int i = 10 / 0;
        log.info("testErrorNum 测试testErrorNum");
        return "testErrorNum -----";
    }

//-------------------------------------- 热点参数 ---------------------------------------------

//    resource	        资源名，必填
//    count	            限流阈值，必填
//    grade	            限流模式	QPS 模式
//    durationInSec	    统计窗口时间长度（单位为秒），1.6.0 版本开始支持
//    controlBehavior	流控效果（支持快速失败和匀速排队模式），1.6.0 版本开始支持
//    maxQueueingTimeMs	最大排队等待时长（仅在匀速排队模式生效），1.6.0 版本开始支持
//    paramIdx	        热点参数的索引，必填
//    paramFlowItemList	参数例外项，可以针对指定的参数值单独设置限流阈值，不受前面 count 阈值的限制。
//    clusterMode	    是否是集群参数流控规则
//    clusterConfig	    集群流控相关配置


/*[
    {
        "resource": "testHotKey",               资源名，必填
        "limitApp": "default",                  控流针对的调用来源，default则不区分调用来源
        "grade": 1,                             QPS 模式 （只支持qps）
        "paramIdx": 0,                          热点参数的索引 (接口的入参索引 0 表示第一个，以此类推)
        "count": 1,                             限流阈值 每秒请求个数
        "controlBehavior": 0,                   流控效果（0支持快速失败，1匀速排队模式）
        "maxQueueingTimeMs": 0,                 最大排队等待时长
        "burstCount": 0,
        "durationInSec": 1,                     统计窗口时间长度（单位为秒）
        "paramFlowItemList": [                  参数列外项 可以针对指定的参数值单独设置限流阈值，不受前面 count 阈值的限制。
            {
                "object": "k",                  参数值
                "count": 10,                    限流阈值 每秒请求个数
                "classType": "java.lang.String" 参数类型 （仅支持基本类型和字符串类型）
            }
		],
        "clusterMode": false,                   是否是集群参数流控规则
        "clusterConfig": {                      集群流控相关配置
            "flowId": null,
            "thresholdType": 0,
            "fallbackToLocalWhenFail": true,
            "sampleCount": 10,
            "windowIntervalMs": 1000
    }
    }
]*/


    /**
     * 热点参数限流
     * 通用设置 ： 每秒只能请求一次
     * 参数列外项 ： p1值为 “k”时， 限流阈值 10
     *
     * @param p1
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "dealTestHotKey",fallback = "dealFallBackHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1) {
//        System.out.println(100/0);
        return "testHotKey -----";
    }

    // sentinel 满足限流规则
    @GetMapping("/dealTestHotKey")
    public String dealTestHotKey(String p1, BlockException e) {
        return p1 + "testHotKey ----- blockHandler";
    }

    // 方法异常
    @GetMapping("/dealFallBackHotKey")
    public String dealFallBackHotKey(String p1) {
        return p1 + "testHotKey ----- fallback";
    }



    @GetMapping("/getProducer")
    public String getProducer(){
        return "hello you get me!";
    }

}
