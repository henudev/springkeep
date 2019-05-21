package com.zfl.ling;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestOrder
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/4/2 16:52
 * @Version 1.0
 **/
@Component
@Order(value = 2)
public class TestOrder implements CommandLineRunner {

    public void run(String[] args) {
        System.out.println("order2");
    }
}
