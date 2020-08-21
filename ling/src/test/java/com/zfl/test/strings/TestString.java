package com.zfl.ling.strings;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName TestString
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/8/11 14:54
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestString {

    @Test
    public void  compareStr() {
        String s1 = "123";
        String s2 = "123";

        boolean result = s1 == s2;
        System.out.println(result);
    }
    
}
