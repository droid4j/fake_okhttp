package com.dapan.okhttp;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class Request {

    final String url;
    final Method method;
    final RequestBody requestBody;

    public Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
    }

    public static class Builder {
        private String url;
        private Method method = Method.GET;
        private RequestBody requestBody;

        public Request build() {
            return new Request(this);
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder post(RequestBody requestBody) {
            this.requestBody = requestBody;
            method = Method.POST;
            return this;
        }

        public Builder get() {
            method = Method.GET;
            return this;
        }
    }
}
