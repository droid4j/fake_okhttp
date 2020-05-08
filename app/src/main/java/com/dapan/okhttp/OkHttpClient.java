package com.dapan.okhttp;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class OkHttpClient {
    final Dispatcher dispatcher;

    public OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
    }

    public OkHttpClient() {
        this(new Builder());
    }

    public Call newCall(Request request) {
        return RealCall.newRealCall(this, request);
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public static final class Builder {
        Dispatcher dispatcher;

        public Builder() {
            dispatcher = new Dispatcher();
        }

        public OkHttpClient build() {
            return new OkHttpClient(this);
        }
    }
}
