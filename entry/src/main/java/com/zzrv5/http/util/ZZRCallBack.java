package com.zzrv5.http.util;


import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 主要用来封装回调操作
 *
 * 创建人:    ZZR老师
 * 创建时间:  2020年9月30日
 */
public abstract class ZZRCallBack<T> {
    static EventHandler mMainHandler = new EventHandler(EventRunner.getMainEventRunner());

    public  void onProgress(float progress, long total ){}

    void onError(final ZZRResponse response){
        
        final String errorMessage;
        if(response.inputStream != null){
            errorMessage = getRetString(response.inputStream);
        }else if(response.errorStream != null) {
            errorMessage = getRetString(response.errorStream);
        }else if(response.exception != null) {
            errorMessage = response.exception.getMessage();
        }else {
            errorMessage = "";
        }
        mMainHandler.postTask(new Runnable() {
            @Override
            public void run() {
                onFailure(response.code,errorMessage);
            }
        });
    }
    void onSeccess(ZZRResponse response){
        final T obj = onParseResponse(response);
        mMainHandler.postTask(new Runnable() {
            @Override
            public void run() {
                onResponse(obj);
            }
        });
    }

    /**
     * 解析response，执行在子线程
     */
    public abstract T onParseResponse(ZZRResponse response);

    /**
     * 访问网络失败后被调用，执行在UI线程
     */
    public abstract void onFailure(int code,String errorMessage);

    /**
     *
     * 访问网络成功后被调用，执行在UI线程
     */
    public abstract void onResponse(T response);


    /**
     * 对操作结果没有过多的耗时操作，或不需要解析JSON，可以使用此默认的回调，直接将响应对象返回。
     */
    public static abstract class ZZRCallBackDefault extends ZZRCallBack<ZZRResponse> {
        @Override
        public ZZRResponse onParseResponse(ZZRResponse response) {
            return response;
        }
    }

    /**
     * 直接返回字符串的相应内容
     */
    public static abstract class ZZRCallBackString extends ZZRCallBack<String> {
        @Override
        public String onParseResponse(ZZRResponse response) {
            try {
                return getRetString(response.inputStream);
            } catch (Exception e) {
                throw new RuntimeException("failure");
            }
        }
    }

    private static String getRetString(InputStream is) {
        String buf;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            buf = sb.toString();
            return buf;

        } catch (Exception e) {
            return null;
        }
    }

}
