package com.dapan.okhttp;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class RequestBody {

    public static final String FORM = "multipart/form-data";
    private Map<String, Object> params;
    private String type;
    private String binary = "OkHttp-" + UUID.randomUUID().toString();
    private String startBinary = "--" + binary;
    private String endBinary = startBinary + "--";

    public RequestBody() {
        this.params = new HashMap<>();
    }

    public RequestBody addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public RequestBody type(String type) {
        this.type = type;
        return this;
    }

    public String getContentType() {
        return type + "; boundary=" + binary;
    }

    private Map<String, String> cache = new HashMap<>();

    public long getContentLength() {
        long length = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String text;
                if (cache.containsKey(key)) {
                    text = cache.get(key);
                } else {
                    text = getText(key, (String) value);
                    cache.put(key, text);
                }
                length += text.getBytes().length;
            } else if (value instanceof Binary) {
                Binary binary = (Binary) value;
                String text = getText(key, binary);
                length += text.getBytes().length;
                length += binary.fileLength() + "\r\n".getBytes().length;
            }
        }

        if (params.size() != 0) {
            length += endBinary.getBytes().length;
        }
        return length;
    }

    public void toWrite(OutputStream outputStream) throws IOException {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String text;
                if (cache.containsKey(key)) {
                    text = cache.get(key);
                } else {
                    text = getText(key, (String) value);
                    cache.put(key, text);
                }
                outputStream.write(text.getBytes());
            } else if (value instanceof Binary) {
                Binary binary = (Binary) value;
                String text = getText(key, binary);
                outputStream.write(text.getBytes());
                binary.onWrite(outputStream);
                outputStream.write("\r\n".getBytes());
            }
        }

        if (params.size() != 0) {
            outputStream.write(endBinary.getBytes());
        }
    }

    private String getText(String key, String value) {
        return startBinary + "\r\n"
                + "Content-Disposition: form-data; name = \"" + key + "\"\r\n"
                + "Context-Type: text/plain\r\n"
                + "\r\n"
                + value
                + "\r\n";
    }

    private String getText(String key, Binary value) {
        return startBinary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + key + "\" filename = \"" + value.getFileName() + "\""
                + "Context-Type: " + value.mimType() + "\r\n"
                + "\r\n\r\n";
    }

    public static Binary create(final File file) {
        return new Binary() {
            @Override
            public String getFileName() {
                return file.getName();
            }

            @Override
            public String mimType() {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String contentTypeFor = fileNameMap.getContentTypeFor(file.getAbsolutePath());
                if (TextUtils.isEmpty(contentTypeFor)) {
                    contentTypeFor = "application/octet-stream";
                }
                return contentTypeFor;
            }

            @Override
            public long fileLength() {
                return file.length();
            }

            @Override
            public void onWrite(OutputStream outputStream) throws IOException {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            }
        };
    }
}
