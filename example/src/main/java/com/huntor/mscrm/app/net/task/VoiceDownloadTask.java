package com.huntor.mscrm.app.net.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.huntor.mscrm.app.utils.Constant;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/16 0016
 * 15:16.
 */
public class VoiceDownloadTask extends AsyncTask<String, Integer, InputStream> {

    private String TAG = getClass().getName();

    private CallBack mCallback;
    private FileOutputStream outputStream;
    private String name;

    public VoiceDownloadTask(CallBack mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected InputStream doInBackground(String... params) {
        InputStream ret = null;

        if (params != null && params.length >= 3) {

            String urlStr = params[0];
            name = params[1] + "." + params[2];
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    ret = new BufferedInputStream(connection.getInputStream());

                    File filePath;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        filePath = Environment.getExternalStorageDirectory();
                    }else {
                        filePath = Environment.getDataDirectory();
                    }

                    String path = filePath.getAbsolutePath() + Constant.VOICE_CACHE_PATH;
                    Log.w(TAG, "path = " + path);
                    File file = new File(path, name);
                    String absolutePath = file.getAbsolutePath();
                    Log.w(TAG, "absolutePath = " + absolutePath);

                    if (!file.exists()){
                        file.createNewFile();
                    }

                    byte[] buffer = new byte[1024];
                    int len = 0;
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        outputStream = new FileOutputStream(file);
                        while ((len = ret.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
//        File filePath;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            filePath = Environment.getExternalStorageDirectory();
//            saveMedia(inputStream, filePath);
//        }else {
//            filePath = Environment.getDataDirectory();
//            saveMedia(inputStream, filePath);
//        }
        mCallback.onResult(name);
    }

    public interface CallBack {
        void onResult(String name);
    }
}
