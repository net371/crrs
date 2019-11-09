package com.crrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.crrs.*.mapper")//扫描的mapper
@SpringBootApplication
@ServletComponentScan
class CrrsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(CrrsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CrrsApplication.class, args);
    }

}
