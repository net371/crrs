package com.crrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.crrs.*.mapper")//扫描的mapper
@SpringBootApplication
@ServletComponentScan
public class CrrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrrsApplication.class, args);
    }

}
