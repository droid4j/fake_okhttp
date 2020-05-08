package com.dapan.okhttp;

import java.io.IOException;

/**
 * Created by per4j
 * on 2020/5/8
 */
public interface Callback {
    void onFailure(Call call, IOException e);

    void onResponse(Call call, Response response) throws IOException;
}
