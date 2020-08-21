package com.zfl.ling.util;

import java.util.ArrayList;
import java.util.List;

public class TestExcption {


    public static void main( String[] args) {
        testModcount2();
    }

        public static void testModcount2() {
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
