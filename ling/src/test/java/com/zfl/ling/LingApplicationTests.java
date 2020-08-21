package com.zfl.ling;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LingApplicationTests {

    @Test
    public void contextLoads() {

    }

    @Test
    public void testModcount() {

        List<String> platformList = new ArrayList<>();
        platformList.add("博客园");
        platformList.add("CSDN");
        platformList.add("掘金");

        for (String platform : platformList) {
            if (platform.equals("博客园")) {
                platformList.remove(platform);
            }
        }

        System.out.println(platformList);
    }



    @Test
    public void testModcount2() {
        List<String> list = new ArrayList<>();
        list.add("s");
        list.add("d");
        list.add("f");

        for (String str : list) {
            if (str.equals("s")) {
                list.remove(str);
            }
        }
        System.out.println(list);
}
}