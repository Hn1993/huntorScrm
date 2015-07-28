package com.huntor.scrm.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by jh on 2015/7/7.
 */
public class WriteJson2SD {
    /*****
     *
     * @param json
     * @param name
     */
    public static void writeJson(String json, String name) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 判断是否存在SD卡
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory()
                .toString()
                + File.separator
                + "cash"
                + File.separator
                + name
                + ".txt");
        if (!file.getParentFile().exists()) {// 判断父文件是否存在，如果不存在则创建
            file.getParentFile().mkdirs();
        }
        PrintStream out = null; // 打印流
        try {
            out = new PrintStream(new FileOutputStream(file)); // 实例化打印流对象
            out.print(json.toString()); // 输出数据
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) { // 如果打印流不为空，则关闭打印流
                out.close();
            }
        }
    }

    //写入sd卡的某个文件下
    public  static void writeJson(String json,String date,String fileName){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 判断是否存在SD卡
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory()
                .toString()
                + File.separator
                + "cash"
                + File.separator
                + fileName
                + ".txt");
        if (!file.getParentFile().exists()) {// 判断父文件是否存在，如果不存在则创建
            file.getParentFile().mkdirs();
        }
        PrintStream out = null; // 打印流
        try {
            out = new PrintStream(new FileOutputStream(file)); // 实例化打印流对象
            out.print(json.toString()+"\n"+date+"\n"); // 输出数据
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) { // 如果打印流不为空，则关闭打印流
                out.close();
            }
        }
    }


}
