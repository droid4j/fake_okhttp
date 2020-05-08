package com.dapan.okhttp;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class Request {

    final String url;

    public Request(Builder builder) {
        this.url = builder.url;
    }

    public static class Builder {
        private String url;

        public Request build() {
            return new Request(this);
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
    }
}
