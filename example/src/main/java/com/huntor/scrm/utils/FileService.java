package com.huntor.scrm.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileService {

	/**
	 * 存文件
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
    public void saveToSDCard(String fileName, String content) throws IOException {  

        File file = new File(Environment.getExternalStorageDirectory(),fileName);  
        if(!file.isDirectory()){
        	file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);  
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();  
    }

	/**
	 * 读取文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
    public static String read(String fileName) throws IOException {
    	File file = new File(Environment.getExternalStorageDirectory(),fileName);
    	if(file.exists()){
    		FileInputStream fileInputStream=new FileInputStream(file); 

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
            byte[] buffer = new byte[1024];  
            int len =0;  

            while((len=fileInputStream.read(buffer))!=-1){  
                outputStream.write(buffer, 0, len);  
            }  

            byte[] data = outputStream.toByteArray();  
            fileInputStream.close();
            return new String(data);  
    	}
    	else
    		return "";
    	
    }

	/**
	 * 保存photo
	 * @param b
	 * @param strFileName
	 */
  	public  void savePhoto(Bitmap b,String strFileName){
  		try {
  			 File file = new File(Environment.getExternalStorageDirectory(),strFileName);  
  		        if(!file.isDirectory()){
  		        	file.createNewFile();
  		        }
  			FileOutputStream fos=new FileOutputStream(file);
  			if(fos!=null){
  				b.compress(Bitmap.CompressFormat.PNG, 80, fos);
  				fos.flush();
  				fos.close();
  			}
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();	
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	}


	/**
	 * 读取photo
	 * @param strFileName
	 * @return
	 */
  	@SuppressWarnings("unused")
	public Bitmap readPhoto(String strFileName){		
  		String path=Environment.getExternalStorageDirectory()+"/"+strFileName;
  		if(path!=null){
  		Bitmap bitmap=BitmapFactory.decodeFile(path);
  		return bitmap;
  		}
  		else
  			return null;
  		
  	}
}
