package com.zfl.ling;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.annotation.Order;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Order(value = 3)
public class LingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LingApplication.class, args);
    }
    
    @Override
    
    public void run(String[] args) throws Exception{
        System.out.println("main");
    }

}
