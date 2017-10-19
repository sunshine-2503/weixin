package com.shaoming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jerry on 17-10-11.
 */
@EnableTransactionManagement // 启用注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class WeixinWebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(WeixinWebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WeixinWebApplication.class, args);
    }

}
