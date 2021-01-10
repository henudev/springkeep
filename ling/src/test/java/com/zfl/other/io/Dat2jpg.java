package com.zfl.other.io;

import java.io.File;

/**
 * @ClassName Dat2jpg
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/5/23 21:28
 * @Version 1.0
 **/

public class Dat2jpg {
    public void change() {
        File file = new File("C:\\Image");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()){
                execu(files[i].toString());
            }else{
                String name = files[i].getAbsolutePath();//newSong\\
                System.out.println("###############name:" + name);
                
                execu(name);
            }

        }
    }
    
    public void execu(String s) {
        System.out.println("start");
        s.replace( "\\\\",   "\\\\\\\\");
        System.out.println("after####################" + s);
        try {
            String ss = "python C:\\" + "\\Image\\" + "\\chatImageDecoder.py " + s;
            Process pr = Runtime.getRuntime().exec(ss);
            pr.waitFor();
            System.out.println("end");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    public static void main (String[] args) {
//        //多线程
//        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 8,5, TimeUnit.SECONDS, 
//                new ArrayBlockingQueue<Runnable>(4), new ThreadPoolExecutor.DiscardOldestPolicy()); 
//        for (int i = 0 ; i < 1000; i++) {
//            String task = "task@ " + i;
//            threadPool.execute(new Dat2jpg().change());            
//        }
//        
//        
        Dat2jpg dat2jpg = new Dat2jpg();
        dat2jpg.change();
    }
}

