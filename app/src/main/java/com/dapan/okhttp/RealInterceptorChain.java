package com.dapan.okhttp;

import java.io.IOException;
import java.util.List;

/**
 * Created by per4j
 * on 2020/5/8
 */
public class RealInterceptorChain implements Interceptor.Chain {

    final List<Interceptor> interceptors;
    final int index;
    final Request request;

    public RealInterceptorChain(List<Interceptor> interceptors, int index, Request request) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response process(Request request) throws IOException {

        // Call the next interceptor in the chain.
        RealInterceptorChain next = new RealInterceptorChain(interceptors, index + 1, request);
        Interceptor interceptor = interceptors.get(index);
        Response response = interceptor.intercept(next);

        return response;
    }
}
