package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.Response;
import com.huntor.mscrm.app.model.UploadResult;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 上传文件
 */
public class ApiUpload {

    private static final String TAG = "ApiUpload";

    private Context mContext;
    private String mUrl;
    private String mType;
    private static final int BUFFER_SIZE = 4 * 1024;

    /**
     * 上传一个文件的路径
     *
     * @param context 上下文
     * @param url     要上传的文件所在的路径
     */
    public ApiUpload(Context context, String url, String type) {
        mContext = context;
        mUrl = url;
        mType = type;
    }

    /**
     * 添加上传返回的结果
     */
    public static class ApiUploadResponse extends BaseResponse {
        public UploadResult result;
    }

    public ApiUploadResponse getHttpResponse() {
        String[] names = mUrl.split("/");
        String name = names[names.length - 1];
        String[] str1 = name.split("\\.");
        String format = "";
        if (str1.length > 0) {
            name = str1[str1.length - 1 - (str1.length - 1)];
            format = str1[str1.length - 1];
        }
        BaseResponse baseResponse = uploadFile(mContext, Constant.HTTP_UP_LOAD_FILE_URL, mUrl, mType, name, format);
        Log.d("--上传文件--", baseResponse.toString());
        ApiUploadResponse response = new ApiUploadResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());
        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.result = gson.fromJson(baseResponse.getContent(), UploadResult.class);
            if (response.result != null && response.result.code.equals("0")) {
                //response.setRetCode(BaseResponse.RET_HTTP_STATUS_ERROR);
                response.setRetInfo(response.result.msg);
                // response.setContent(baseResponse.getContent());
            }
        }
        return response;
    }


    /* 上传文件至Server的方法 */
    private BaseResponse uploadFile(Context context, String urls, String uploadFile, String type, String name, String format) {
        BaseResponse baseResponse = new BaseResponse();
        DataOutputStream ds = null;
        FileInputStream fStream = null;
        try {
            URL url = new URL(urls);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "ISO-8859-1");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("type", type);
            con.setRequestProperty("name", name);
            con.setRequestProperty("format", format);
            con.setConnectTimeout(10 * 1000);

			/* 设置DataOutputStream */
            ds = new DataOutputStream(con.getOutputStream());
            /* 取得文件的FileInputStream */
            fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将文件写入DataOutputStream中 */
                ds.write(buffer, 0, length);
                Log.e(TAG, "写入" + length);
            }
            /* close streams */
            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String charSet = "ISO-8859-1";
            String entityStr = new String(b.toString().trim().getBytes(charSet), "utf-8");
            baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_OK);
            baseResponse.setContent(entityStr);
            /* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            Log.e("上传测试类", "上传失败" + e);
            baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_ERROR);
            baseResponse.setRetInfo(context.getString(R.string.http_error));
            e.printStackTrace();
        } finally {
            try {
                if (ds != null) {
                    ds.close();
                }
                if (fStream != null) {
                    fStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baseResponse;
    }

}
