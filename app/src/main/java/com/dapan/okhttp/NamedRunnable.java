package com.dapan.okhttp;

/**
 * Created by per4j
 * on 2020/5/8
 */
public abstract class NamedRunnable implements Runnable {

    @Override public final void run() {
        execute();
    }

    protected abstract void execute();
}
