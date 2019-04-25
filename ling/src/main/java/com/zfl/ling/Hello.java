package com.zfl.ling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName Hello
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/4/1 17:24
 * @Version 1.0
 **/
@Controller
@Order(value = 1)
public class Hello implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(Hello.class);
        @RequestMapping("/zhangfangling")
        public String sayHello() {
            return "hello";
        }
    @Override

    public void run(String[] args) throws Exception{
        System.out.println("order1");
        logger.info("sssssssssssssssssssssssss");
        logger.debug("##########debug");
        logger.warn("#############warn");
        logger.error("$$$$$$$$$$error");
    }
}
