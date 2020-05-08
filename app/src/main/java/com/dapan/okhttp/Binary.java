package com.dapan.okhttp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by per4j
 * on 2020/5/8
 */
public interface Binary {

    String getFileName();
    String mimType();
    long fileLength();
    void onWrite(OutputStream outputStream) throws IOException;
}
