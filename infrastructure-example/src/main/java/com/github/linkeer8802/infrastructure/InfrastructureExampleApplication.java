package com.github.linkeer8802.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class InfrastructureExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfrastructureExampleApplication.class, args);
    }
}

