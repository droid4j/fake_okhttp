package com.dapan.okhttp;

import android.util.Log;

import java.io.IOException;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class BridgeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG", "BridgeInterceptor");
        Request request = chain.request();

        request.addHeader("Connection", "Keep-Alive");

        RequestBody body = request.requestBody;
        if (body != null) {
            long contentLength = body.getContentLength();
            request.addHeader("Content-Length", Long.toString(contentLength));
            String contentType = body.getContentType();
            request.addHeader("Content-Type", contentType);
        }

        Response process = chain.process(request);

        return process;
    }
}
