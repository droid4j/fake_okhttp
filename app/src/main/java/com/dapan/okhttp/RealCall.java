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
public class RealCall implements Call {
    final OkHttpClient client;
    final Request originalRequest;

    public RealCall(OkHttpClient client, Request request) {
        this.client = client;
        this.originalRequest = request;
    }

    public static RealCall newRealCall(OkHttpClient okHttpClient, Request request) {

        return new RealCall(okHttpClient, request);
    }

    @Override
    public void enqueue(Callback callback) {

        client.dispatcher().enqueue(new RealCall.AsyncCall(callback));

    }

    class AsyncCall extends NamedRunnable {

        private Callback callback;
        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected void execute() {
            // 最终来到这里执行
            try {
                final Request request = originalRequest;
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
                    Response response = new Response(inputStream);
                    callback.onResponse(RealCall.this, response);
                } else {
                    callback.onFailure(RealCall.this, new IOException("status code: " + statusCode));
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(RealCall.this, e);
            }
        }
    }
}
