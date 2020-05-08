package com.dapan.okhttp;

import java.io.IOException;
import java.io.OutputStream;
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
}
