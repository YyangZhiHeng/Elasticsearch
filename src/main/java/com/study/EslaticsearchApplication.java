package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.study.mapper")
@SpringBootApplication
public class EslaticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(EslaticsearchApplication.class, args);
    }

}
