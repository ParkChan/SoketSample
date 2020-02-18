package com.socket.socket.common;


import com.socket.socket.constant.NetworkConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketSinglton {

    private static Socket socket = null;

    static {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = true;
            options.reconnectionDelay = 1000;
            options.reconnectionDelayMax = 5000;
            options.reconnectionAttempts = 10;
            socket = IO.socket(NetworkConstant.SOCKET_SERVER_URL, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Socket getInstance() {
        return socket;
    }
}

