package com.dapan.okhttp;

import java.io.IOException;

/**
 * Created by per4j
 * on 2020/5/8
 */
public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain {
        Request request();
        Response process(Request request) throws IOException;
    }
}
