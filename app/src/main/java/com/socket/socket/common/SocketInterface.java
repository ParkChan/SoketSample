package com.socket.socket.common;

public interface SocketInterface {
    void onConnect(Object... args);
    void onDisconnect(Object... args);
    void onConnectError(Object... args);
    void onConnectTimeOut(Object... args);
    void onMessageReceived(Object... args);
}
