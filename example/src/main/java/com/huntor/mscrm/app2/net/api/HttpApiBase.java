package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.CustomHttpClient;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.push.PushMessageReceiverService;
import com.huntor.mscrm.app2.ui.MainActivity2;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 网络访问API接口的基类，各网络访问接口都需要继承该类
 */
public abstract class HttpApiBase {

    private static final boolean DEBUG = true;
    private static final String TAG = "HttpApiBase";

    private static final int BUFFER_SIZE = 4 * 1024;
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=utf-8";//application/x-www-form-urlencoded

    protected Context mContext;
    protected HttpRequest mHttpRequest; // Http访问方式及参数

    public HttpApiBase(Context context) {
        mContext = context;
    }

    /**
     * 发送HTTP请求
     *
     * @return HttpResponse
     */
    private HttpResponse requestHttp() {

        if (mHttpRequest == null) {
            return null;
        }
        String url = mHttpRequest.getUrl(); // 获取请求的url
        int requestMethod = mHttpRequest.getRequestMethod(); // 获取请求的方式

        HttpResponse response = null;
        HttpUriRequest httpRequest = null;
        BaseRequestParams params = mHttpRequest.getmParams(); // 获取请求参数
        String token = PreferenceUtils.getString(mContext, Constant.PREFERENCE_TOKEN, "");
        boolean isLogin = url.contains(Constant.HTTP_REQUEST_USER_LOGIN);
        MyLogger.i(TAG,"token: "+token);
        MyLogger.i(TAG,"url: "+url);
        MyLogger.i(TAG,"isLogin: "+isLogin);
        try {
            switch (requestMethod) {
                // HTTP_GET
                case HttpRequest.REQUEST_METHOD_HTTP_GET:
                    if (params != null) {
                        url += params.generateRequestParams();// 将请求参数拼接在url中
                    }
                    httpRequest = new HttpGet(url);

                    if (TextUtils.isEmpty(token) && !isLogin) {
                        logout();
                    }else if(!TextUtils.isEmpty(token)&&!isLogin){
                        httpRequest.setHeader("token", token);
                    }

                    break;
                // HTTP_POST
                case HttpRequest.REQUEST_METHOD_HTTP_POST:
                    httpRequest = new HttpPost(url);
                    if (TextUtils.isEmpty(token) && !isLogin) {
                        logout();
                    }else if(!TextUtils.isEmpty(token)&&!isLogin){
                        httpRequest.setHeader("token", token);
                    }
                    if (params != null) {
                    /*
                     * post请求需要把参数放到entity，这里放入的是表单数据(key1=value1&key2=value2)。
                     * 需要注意的是，通常情况下表单数据使用UrlEncodedFormEntity。这里为了和get请求保持
                     * 一致，使用的是StringEntity
                     * ，必须设置请求数据的类型为"application/x-www-form-urlencoded",
                     * 同时需要把generateRequestParams生成的请求参数头部的"?"去掉。
                     */
                        Log.i(TAG, "post params = " + params.generateRequestParams());

                        StringEntity se = new StringEntity(params.generateRequestParams().replaceFirst(
                                "\\?", ""), HTTP.UTF_8);

                        Log.e(TAG, "参数=token=" + token + "&" + params.generateRequestParams().replaceFirst("\\?", ""));

                        se.setContentType(CONTENT_TYPE_FORM);
//                    Log.e(TAG,"post params==="+inputStream2String(se.getContent()));
                        ((HttpPost) httpRequest).setEntity(se);
                        //httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    }
                    break;
                // HTTP_PUT
                case HttpRequest.REQUEST_METHOD_HTTP_PUT:
                    break;
                // HTTP_DELETE
                case HttpRequest.REQUEST_METHOD_HTTP_DELETE:
                    break;
            }
            Log.i(TAG, "http--request url = " + url);
            response = CustomHttpClient.execute(mContext, httpRequest); // 通过HttpClient发送网络请求
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 将HttpResponse转换为BaseResponse
     *
     * @return BaseResponse
     */
    protected BaseResponse getHttpContent() {
        BaseResponse baseResponse = new BaseResponse();
        HttpResponse httpResponse = requestHttp();

        // 当网络访问异常时返回自定义的错误信息
        if (httpResponse == null) {
            if (DEBUG) {
                Log.d(TAG, "http--response = null");
            }
            baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_ERROR);
            baseResponse.setRetInfo(mContext.getString(R.string.http_error));
            return baseResponse;
        }

        int status = httpResponse.getStatusLine().getStatusCode();
        baseResponse.setRetCode(status);
        baseResponse.setRetInfo(httpResponse.getStatusLine().getReasonPhrase());
        if (DEBUG) {
            Log.d(TAG, "http--statusCode = " + status);
            Log.d(TAG, "http--statusInfo = " + httpResponse.getStatusLine().getReasonPhrase());
            if (status != 200) {
                try {
                    Log.d(TAG, "http--content = "
                            + inputStream2String(httpResponse.getEntity().getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        switch (status) {
            case 200:
                InputStream in = null;
                try {
                    in = getISFromRespone(httpResponse);
                    if (mHttpRequest != null
                            && mHttpRequest.getResponseType() == HttpRequest.RESPONSE_TYPE_XML) {
                        baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_OK);
                        baseResponse.setContent(inputStream2String(in));
                    } else {
                        parseResult(baseResponse, inputStream2String(in));
                    }
                } catch (Exception e) {
                    if (DEBUG) {
                        Log.d(TAG, "HttpContent--exception--\n" + e.toString());
                    }
                    baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_ERROR);
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "Response--code = " + baseResponse.getRetCode());
            Log.d(TAG, "Response--content = " + baseResponse.getContent());
        }
        return baseResponse;
    }

    /**
     * 获取输入流
     *
     * @param response
     * @return
     */
    private InputStream getISFromRespone(HttpResponse response) {
        try {
            if (mHttpRequest.isCacheable()) {
                String filePath = writeInputSteamToCache(response.getEntity().getContent());
                if (!TextUtils.isEmpty(filePath)) {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
                    return bis;
                }
            } else {
                return new BufferedInputStream(response.getEntity().getContent());
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "write-cache--error" + e.toString());
            }
            return null;
        }
        return null;
    }

    /**
     * 将数据写入缓存
     *
     * @param is 输入流
     * @return 缓存的文件路径
     */
    private String writeInputSteamToCache(InputStream is) {
        String cachedir = Utils.getCacheDir(mContext);
        try {
            File cache = new File(cachedir);
            if (!cache.exists()) {
                cache.mkdirs();
            }
            BufferedInputStream bis = new BufferedInputStream(is);
            final String fileName = mHttpRequest.getMd5Key();
            File file = new File(cachedir, fileName);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            bis.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "writecache" + e.toString());
            }
            return null;
        }
    }

    /**
     * 将InputStream转换为String
     *
     * @param is InputStream
     * @return String
     */
    private String inputStream2String(InputStream is) {
        if (is == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            sb.append(baos.toString("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 解析服务器返回的result字段
     *
     * @param baseResponse 自定义的Response，对HttpRespone做了进一步的封装
     * @param content      服务端传回的数据
     */
    private void parseResult(BaseResponse baseResponse, String content) {
        Log.e(TAG, "111--content-->>>" + content);
        try {
            JSONObject obj = new JSONObject(content); // 用返回的json串生成JSONObject
            String result = obj.getString("code"); // 获取result的值，0表示成功，其它表示失败
            if (TextUtils.equals(result, "0")) {
                // 成功
                baseResponse.setContent(content);
                baseResponse.setRetCode(BaseResponse.RET_HTTP_STATUS_OK);
                baseResponse.setRetInfo(baseResponse.getRetInfo());
            } else {
                // 失败
                baseResponse.setRetCode(BaseResponse.RET_RESULT_STATUS_ERROR);
                baseResponse.setRetInfo(obj.getString("msg"));
                //失败的情况   发现其他设备登录的时候被干掉
                MyLogger.e(TAG,"logout: "+result);
                if (TextUtils.equals(result, "101")) {
                    logout();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Intent serviceIntent = new Intent(mContext, PushMessageReceiverService.class);
        mContext.stopService(serviceIntent);
        String pwd = PreferenceUtils.getString(mContext, Constant.PREFERENCE_PSW, "");
        PreferenceUtils.putString(mContext, Constant.PREFERENCE_PSW_RELOGIN, pwd);
        //PreferenceUtils.clearString(mContext, Constant.PREFERENCE_PSW);
        Intent intent = new Intent(mContext, MainActivity2.class);
        intent.putExtra(Constant.LOGOUT, Constant.LOGOUT_FLAG);
        mContext.startActivity(intent);
    }

}
