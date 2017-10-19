package com.shaoming;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jerry on 17-10-11.
 */
@SpringBootApplication
@MapperScan("com.shaoming.*.mapper")
public class WeixinCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinCoreApplication.class, args);
    }

}
