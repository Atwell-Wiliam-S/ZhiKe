package com.zhike.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        executeSqlScript("init.sql");
    }
    
    private void executeSqlScript(String scriptName) throws IOException {
        ClassPathResource resource = new ClassPathResource(scriptName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String sql = reader.lines().collect(Collectors.joining("\n"));
            // 分割SQL语句并执行
            String[] statements = sql.split(";\s*");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    try {
                        jdbcTemplate.execute(statement);
                    } catch (Exception e) {
                        // 忽略已存在的表错误
                        if (!e.getMessage().contains("already exists")) {
                            throw e;
                        }
                    }
                }
            }
        }
    }
}
