package com.entermoor.polyfiter.utils;

public class GwtRunnablePoster implements IRunnablePoster {
    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }

    @Override
    public int properRunnableNumber() {
        return 1;
    }
}
