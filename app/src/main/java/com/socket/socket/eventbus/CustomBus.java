package com.socket.socket.eventbus;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Event bus [Bus "default"] accessed from non-main thread Looper
 */
public class CustomBus extends Bus {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(() -> CustomBus.super.post(event));
        }
    }
}
