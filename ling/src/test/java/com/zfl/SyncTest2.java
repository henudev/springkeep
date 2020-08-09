package com.zfl;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SyncTest
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/5/20 15:35
 * @Version 1.0
 **/

public class SyncTest2 {
    public synchronized void test01() throws InterruptedException {
        System.out.println("tag-a------------");
        Thread.sleep(4000);
        System.out.println("睡了四秒之后");
        Map map = new HashMap();
    }
    
    public void test02() {
        System.out.println("excute test02 method");
    }
    
    public static void main(String[] args) {
        SyncTest2 syncTest1 = new SyncTest2();
        
        Thread thread1 = new Thread(() -> {
            try {
                syncTest1.test01();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            syncTest1.test02();
        });
        thread1.start();
        thread2.start();            
    }
}
