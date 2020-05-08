package com.dapan.okhttp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class Dispatcher {

    private ExecutorService executorService;

    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return executorService;
    }

    public void enqueue(RealCall.AsyncCall asyncCall) {
        getExecutorService().execute(asyncCall);
    }
}
