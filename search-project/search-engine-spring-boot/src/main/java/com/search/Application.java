package com.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.search.dao")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("http://localhost:9090/search");
        SpringApplication.run(Application.class, args);
    }
}
