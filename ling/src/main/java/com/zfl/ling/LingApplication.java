package com.zfl.ling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.zfl.ling.dao")

public class LingApplication implements CommandLineRunner {



    public static void main(String[] args) {
        SpringApplication.run(LingApplication.class, args);
//        testModcount2();
    }
    
    @Override
    public void run(String[] args) throws Exception{
        System.out.println("main");
    }
    
}
