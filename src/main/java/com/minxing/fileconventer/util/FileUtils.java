package com.minxing.fileconventer.util;

import java.io.File;
import java.util.List;

/**
 * 文件操作工具类
 */
public class FileUtils {
     public  static  boolean  scanFile(String dirPath,String fileName){
         File dir=new File(dirPath);
          String[] list=    dir.list();
           for (int i=0;i<list.length;i++){
               if (list[i].equals(fileName)){
                   return true;
               }
           }
           return  false;
     }

    public static void main(String[] args) {
         boolean s=   scanFile("D:/tmp/conventer","审流程需求说明.pdf");
        System.out.println(s);
    }
}
