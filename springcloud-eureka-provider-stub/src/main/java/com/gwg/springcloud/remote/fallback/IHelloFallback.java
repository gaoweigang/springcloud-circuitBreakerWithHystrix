package com.gwg.springcloud.remote.fallback;

import com.gwg.springcloud.zuul.common.Result;
import com.gwg.springcloud.model.User;
import com.gwg.springcloud.remote.IHelloRemote;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 熔断处理
 */
@Component
@Slf4j
public class IHelloFallback implements FallbackFactory<IHelloRemote> {

    public IHelloRemote create(Throwable throwable) {
        final StringBuffer message = new StringBuffer("IHelloRemote fallback");

        //在这里打印出异常，调试用
        throwable.printStackTrace();

        if(throwable != null && throwable instanceof HystrixTimeoutException){
            message.append(" timeout ");
            log.error(message.toString(), throwable);

        }
        return new IHelloRemote(){
            private String msg = message.toString();

            public Result printUserInfo(String name, int age) {
                return Result.error(msg);
            }

            public Result getUser(String name) {
                return Result.error(msg);
            }

            public Result queryUserInfo(String name) {
                return Result.error(msg);
            }
        };
    }
}
