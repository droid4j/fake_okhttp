package com.dapan.okhttp;

import android.util.Log;

import java.io.IOException;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class RealCall implements Call {
    final OkHttpClient client;
    final Request request;

    public RealCall(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
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
            Response response = new Response();
            try {
                Log.e("TAG", "execute: 开始请求：" + request.url);
                callback.onResponse(RealCall.this, response);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(RealCall.this, e);
            }
        }
    }
}
