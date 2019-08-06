package com.gwg.springcloud.provider;

import com.gwg.springcloud.zuul.common.Result;
import com.gwg.springcloud.model.User;
import com.gwg.springcloud.remote.IHelloRemote;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 在实际项目中，都是使用声明式调用服务。而不会在客服端和服务端存储2份相同的model和api定义。
 * Restful接口的第二种写法,在使用Eureka时候推荐。因为这样可以将Eureka服务提供方(IHelloRemote)单独抽象到一个模块中,
 * 这样做，Eureka服务提供方方便实现并且可以提供resful服务和Eureka使用方仅需要依赖就可以使用了，
 * 否则服务提供方与服务访问方都需要改动。
 */
@RestController
public class HelloController implements IHelloRemote {

    private volatile int count = 0;


    /**
     * 使用默认的断路器设置
     * 通过 @HystrixCommand 实现 服务提供方 短路、并进行短路处理。短路配置使用yml中全局配置。如果yml中没有配置，则使用默认配置
     * @param name
     * @param age
     * @return
     */
    @HystrixCommand(fallbackMethod = "printUserInfoFallback")
    public Result printUserInfo(@PathVariable("name") String name, @PathVariable("age") int age) {
        count++;
        String message = "姓名: " + name+", 年龄:"+ age+", count: "+ count;
        System.out.println(message);
        int a = count / 0;
        return Result.success();
    }

    /**
     * 测试熔断,@HystrixCommand配置会覆盖yml中的全局配置，因此熔断降级需要单独再配置
     * @return
     */
    @HystrixCommand(commandKey = "threadKey1",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="THREAD"),
                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds" , value = "3000"), //注意：单位是毫秒
                    // 熔断配置
                    //当在配置时间窗口(默认值是10秒)内达到此数量的失败后进行短路，默认20个。该属性用来设置在滚动时间窗中，断路器的最小请求数。
                    //例如：默认值 20 的情况下，如果滚动时间窗（默认值 10秒）内仅收到19个请求，即使这19个请求都失败了，断路器也不会打开。
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="50"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="60000") //注意：单位是毫秒
            },
            fallbackMethod = "getUserFallback")//服务降级，包含熔断降级，异常处理降级
    public Result getUser(@PathVariable("name") String name) {
        count++;
        String message = "姓名: " + name+",  count:" + count;
        System.out.println(message);
        int a = count / 0;//模拟一个时间窗口内失败20次的场景
        return Result.success(message);
    }


    /**
     * 服务提供方进行熔断，降级，限流都要使用@HystrixCommand来做？
     * @param name
     * @return
     */
    public Result queryUserInfo(@PathVariable("name") String name) {
        count++;
        String message = "姓名: " + name+", count: "+ count;
        System.out.println(message);
        int a = count / 0;
        return Result.success();
    }


    private Result getUserFallback(String name){
        return Result.error();
    }

    private Result printUserInfoFallback(String name, int age){
        return Result.error();
    }
}
