package com.zhike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 智能教学系统主应用类
 * Spring Boot应用的入口点
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.zhike")
@EntityScan(basePackages = "com.zhike")
@EnableJpaRepositories(basePackages = "com.zhike")
public class Application {

    /**
     * 应用程序入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}