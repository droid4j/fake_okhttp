package com.dapan.okhttp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                Response response = getResponseWithInterceptorChain();
                if (response.isSuccess()) {
                    callback.onResponse(RealCall.this, response);
                } else {
                    callback.onFailure(RealCall.this, new IOException("status code: " + response.getStatusCode()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFailure(RealCall.this, e);
            }
        }
    }

    Response getResponseWithInterceptorChain() throws IOException {
        List<Interceptor> interceptors = new ArrayList<>();

        interceptors.add(new BridgeInterceptor());
        interceptors.add(new CallServerInterceptor());

        RealInterceptorChain chain = new RealInterceptorChain(interceptors, 0, originalRequest);
        return chain.process(originalRequest);
    }
}
