package com.dapan.okhttp;

/**
 * Created by per4j
 * on 2020/5/8
 */
public enum Method {

    GET("GET"), POST("POST");

    String name;

    Method(String name) {
        this.name = name;
    }

    public boolean doOutput() {
        switch (this) {
            case POST:
                return true;
        }
        return false;
    }
}
