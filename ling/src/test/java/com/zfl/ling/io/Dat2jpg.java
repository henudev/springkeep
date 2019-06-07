package com.zfl.ling.io;

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
        File file = new File("C:\\Image\\2019-02");
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
            String ss = "python C:\\" + "\\Image\\" + "\\2019-02\\" + "\\chatImageDecoder.py " + s;
            Process pr = Runtime.getRuntime().exec(ss);
            pr.waitFor();
            System.out.println("end");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    public static void main (String[] args) {
        Dat2jpg dat2jpg = new Dat2jpg();
        dat2jpg.change();
    }
}

