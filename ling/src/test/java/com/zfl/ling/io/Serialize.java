package com.zfl.ling.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @ClassName Serialize
 * @Description TODO
 * @Author zzzzitai
 * @Date 2019/5/21 11:22
 * @Version 1.0
 **/

public class Serialize implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int num = 1390;
    public static void main(String[] args) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("d:/serialize.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            Serialize serialize = new Serialize();
            oos.writeObject(serialize);
            oos.flush();
            oos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    
}
