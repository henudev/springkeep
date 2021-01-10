package com.zfl.other.lambda;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestFuction1 {
    @Test
    public void test1(){
        Runnable r = () -> {
            System.out.println("hello world");
            new Exception("dddd");
        };
    }

}
