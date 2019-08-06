package com.gwg.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 *
 *
 *
 * @EnableFeignClients注解
 */
@SpringBootApplication
//服务注册与发现，即Eureka客户端服务提供方用于服务注册，Eureka客户端服务访问方用于服务发现
@EnableEurekaClient
//开启Feign客户端,声明式调用服务使用
@EnableFeignClients(basePackages = "com.gwg.springcloud.remote")//basePackages指定扫描目录
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class);
    }
}