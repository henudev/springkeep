package com.zfl.ling;

/**
 * @ClassName SyncTest
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/5/20 15:35
 * @Version 1.0
 **/

public class SyncTest {
    private int num = 0;
    public synchronized void testPrintNum(String tag) throws InterruptedException {
        if ("a".equals(tag)) {
            num = 100;
            System.out.println("tag-a------------");
            Thread.sleep(1000);
        }else {
            num = 200;
            System.out.println("tag-b----------");
        }
        System.out.println("tag:" + tag + ",num:" + num);
    }
    
    public static void main(String[] args) {
        SyncTest syncTest1 = new SyncTest();
        SyncTest syncTest2 = new SyncTest();
        
        Thread thread1 = new Thread(() -> {
            try {
                syncTest1.testPrintNum("a");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                syncTest2.testPrintNum("b");                
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();            

    }
}
