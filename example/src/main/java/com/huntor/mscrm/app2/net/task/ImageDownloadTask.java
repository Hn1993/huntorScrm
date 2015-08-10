package com.huntor.mscrm.app2.net.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.huntor.mscrm.app2.utils.Constant;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/16 0016
 * 15:16.
 */
public class ImageDownloadTask extends AsyncTask<String, Void, String> {

    private String TAG = getClass().getName();

    private CallBack mCallback;



    public ImageDownloadTask(CallBack mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        FileOutputStream outputStream = null;
        InputStream ret = null;
        String path = null;
        if (params != null && params.length == 1) {

            String urlStr = params[0];
            try {

                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() == 200){

                   ret = new BufferedInputStream(connection.getInputStream());
                }


                    File filePath;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        filePath = Environment.getExternalStorageDirectory();
                    }else {
                        filePath = Environment.getDataDirectory();
                    }

                    path = filePath.getAbsolutePath() + Constant.IMAGE_CACHE_PATH;
                    File file = new File(path);
                    Log.w(TAG, "path = " + path);




                    File newfile = new File(path, new Date().getTime() + ".jpg");

                    String absolutePath = newfile.getAbsolutePath();
                    Log.w(TAG, "absolutePath = " + absolutePath);

                    if (!file.exists()){
                        file.mkdirs();
                    }

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    try {
                        if (!newfile.exists()) {
                            newfile.createNewFile();
                        }
                        outputStream = new FileOutputStream(newfile);
                        while ((len = ret.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }

                            if (ret != null) {
                                ret.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }

        return path;
    }

    @Override
    protected void onPostExecute(String result) {
//        File filePath;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            filePath = Environment.getExternalStorageDirectory();
//            saveMedia(inputStream, filePath);
//        }else {
//            filePath = Environment.getDataDirectory();
//            saveMedia(inputStream, filePath);
//        }
        mCallback.onResult(result);
    }

    public interface CallBack {
        void onResult(String result);
    }
}
