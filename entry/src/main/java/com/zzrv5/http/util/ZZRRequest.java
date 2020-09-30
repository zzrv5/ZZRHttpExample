package com.zzrv5.http.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 主要通过请求获得相应的工具类
 * 创建人:    ZZR老师
 * 创建时间:  2020年9月30日
 */

 class ZZRRequest {


    /**
     * get请求
     */
    ZZRResponse getData(String requestURL, Map<String, String> headerMap){
        HttpURLConnection conn = null;
        try {
            conn= getHttpURLConnection(requestURL,"GET");
            conn.setDoInput(true);
            if(headerMap != null){
                setHeader(conn,headerMap);
            }
            conn.connect();
            return getRealResponse(conn);
        } catch (Exception e) {
            return getExceptonResponse(conn, e);
        }
    }

    /**
     * post请求
     */
    ZZRResponse postData(String requestURL, String body, String bodyType, Map<String, String> headerMap) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpURLConnection(requestURL,"POST");
            conn.setDoOutput(true);//可写出
            conn.setDoInput(true);//可读入
            conn.setUseCaches(false);//不是有缓存
            if(!TextUtils.isEmpty(bodyType)) {
                conn.setRequestProperty("Content-Type", bodyType);
            }
            if(headerMap != null){
                setHeader(conn,headerMap);//请求头必须放在conn.connect()之前
            }
            conn.connect();// 连接，以上所有的请求配置必须在这个API调用之前
            if(!TextUtils.isEmpty(body)) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(body);
                writer.close();
            }
            return getRealResponse(conn);
        } catch (Exception e) {
            return getExceptonResponse(conn, e);
        }
    }

    /**
     * 得到Connection对象，并进行一些设置
     */
    private HttpURLConnection getHttpURLConnection(String requestURL,String requestMethod) throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10*1000);
        conn.setReadTimeout(15*1000);
        conn.setRequestMethod(requestMethod);
        return conn;
    }

    /**
     * 设置请求头
     */
    private void setHeader(HttpURLConnection conn, Map<String, String> headerMap) {
        if(headerMap != null){
            for (String key: headerMap.keySet()){
                conn.setRequestProperty(key, headerMap.get(key));
            }
        }
    }

    /**
     * 当正常返回时，得到Response对象
     */
    private ZZRResponse getRealResponse(HttpURLConnection conn) throws IOException {
        ZZRResponse response = new ZZRResponse();
        response.code = conn.getResponseCode();
        response.contentLength = conn.getContentLength();
        response.inputStream = conn.getInputStream();
        response.errorStream = conn.getErrorStream();
        return response;
    }

    /**
     * 当发生异常时，得到Response对象
     */
    private ZZRResponse getExceptonResponse(HttpURLConnection conn, Exception e) {
        if(conn != null){
            conn.disconnect();
        }
        e.printStackTrace();
        ZZRResponse response = new ZZRResponse();
        response.exception = e;
        return response;
    }
}
