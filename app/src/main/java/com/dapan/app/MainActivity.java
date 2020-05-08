package com.dapan.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dapan.okhttp.Call;
import com.dapan.okhttp.Callback;
import com.dapan.okhttp.OkHttpClient;
import com.dapan.okhttp.Request;
import com.dapan.okhttp.RequestBody;
import com.dapan.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OkHttpClient 负责创建Dispatcher ，用于线程分发
        //                  负责关联Call，Call是一个规范，比如：同步执行还是异步执行
        // 执行Call的 enqueue 就是创建一个线程，并提交到线程池，然后就会执行它的execute()方法，用于执行网络请求

        // 其实，到这里，我们的OkHttpClient只是一个线程调度器而已，并没有进行网络操作
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new RequestBody().addParam("username", "test")
                .addParam("password", "123123").type(RequestBody.FORM);
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/user/login")
                .post(requestBody)
                .build();
        Call newCall = client.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "onResponse: " + response.string());
            }
        });
    }
}
