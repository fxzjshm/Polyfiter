package com.entermoor.polyfiter.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PooledRunnablePoster implements IRunnablePoster {
    public static ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void post(Runnable runnable) {
        pool.execute(runnable);
    }
}
