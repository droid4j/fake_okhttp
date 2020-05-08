package com.dapan.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class CallServerInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG", "CallServerInterceptor");
        Request request = chain.request();

        Log.e("TAG", "execute: 开始请求：" + request.url);

        URL url = new URL(request.url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        System.out.println(request.method.name);
        urlConnection.setDoOutput(request.method.doOutput());
        urlConnection.setRequestMethod(request.method.name);

        RequestBody requestBody = request.requestBody;
        if (requestBody != null) {
            String contentType = requestBody.getContentType();
            Log.e("TAG", "contentType: " + contentType);
            urlConnection.setRequestProperty("Content-Type", contentType);
            long contentLength = requestBody.getContentLength();
            Log.e("TAG", "contentLength: " + contentLength);
            urlConnection.setRequestProperty("Content-Length", Long.toString(contentLength));
        }

        urlConnection.connect();

        if (requestBody != null) {
            requestBody.toWrite(urlConnection.getOutputStream());
        }

        int statusCode = urlConnection.getResponseCode();
        if (statusCode == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            return new Response(200, inputStream);
        } else {
            return new Response(statusCode);
        }
    }
}
