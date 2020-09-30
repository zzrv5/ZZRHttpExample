package com.zzrv5.http.util;

import java.io.InputStream;

/**
 * Http响应封装类
 * 创建人:    ZZR老师
 * 创建时间:  2020年9月30日
 */
public class ZZRResponse {
    public InputStream inputStream;
    public InputStream errorStream;
    public int code;
    public long contentLength;
    public Exception exception;
}
