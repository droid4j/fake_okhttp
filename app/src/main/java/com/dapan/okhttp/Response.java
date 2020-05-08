package com.dapan.okhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class Response {

    private InputStream inputStream;
    private int statusCode;

    public Response(int statusCode) {
        this(statusCode, null);
    }

    public Response(int statusCode, InputStream inputStream) {
        this.statusCode = statusCode;
        this.inputStream = inputStream;
    }

    public boolean isSuccess() {
        return statusCode == 200;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String string() {
        return convertToString(inputStream);
    }

    private String convertToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
