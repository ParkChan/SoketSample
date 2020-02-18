package com.socket.socket;

import com.socket.socket.common.SocketInterface;
import com.socket.socket.common.SocketSinglton;
import com.socket.socket.constant.NetworkConstant;
import com.socket.socket.eventbus.OrderEventProvider;
import com.socket.socket.util.LogUtil;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketMannager {

    private final String TAG = SocketMannager.class.getSimpleName();
    private Socket socket;
    private SocketInterface socketInterface;

    private static class SingletonHolder {
        private static final SocketMannager INSTANCE = new SocketMannager(SocketSinglton.getInstance());
    }

    public static SocketMannager getInstance() {
        return SocketMannager.SingletonHolder.INSTANCE;
    }

    private SocketMannager(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String event, String message) {
        socket.emit(event, message);
    }

    public void socketOn() {
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeOut);
        socket.on(NetworkConstant.SOCKET_NEW_MESSAGE, onMessageReceived);     //서버와 규약한 Event Type
        socket.connect();
    }

    public void socketOff() {
        socket.disconnect();
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.off(NetworkConstant.SOCKET_NEW_MESSAGE, onMessageReceived);
    }

    private Emitter.Listener onConnect = args -> {
        LogUtil.d(TAG, "onConnect");
        socketInterface.onConnect(args);
        socket.emit(NetworkConstant.SOCKET_ADD_USER, "chan");
    };

    private Emitter.Listener onDisconnect = args -> {
        LogUtil.d(TAG, " onDisconnect");
        socketInterface.onDisconnect(args);
    };

    private Emitter.Listener onConnectError = args -> {
        LogUtil.d(TAG, " onConnectError");
        socketInterface.onConnectError(args);
    };

    private Emitter.Listener onConnectTimeOut = args -> {
        LogUtil.d(TAG, " onConnectTimeOut");
        socketInterface.onConnectTimeOut(args);
    };

    private Emitter.Listener onMessageReceived = args -> {
        LogUtil.d(TAG, " onMessageReceived");
        socketInterface.onMessageReceived(args);
    };

    /**
     * 소켓통신으로 전달 받은 데이터를 UI로 전달
     * @param message 메시지
     */
    public void sendData(String message) {
        OrderEventProvider.getInstance().post(message);
    }

    /**
     * EventBus Register
     * onCreate 에서 호출
     */
    public void registerEventBus(Object object){
        OrderEventProvider.getInstance().register(object);
    }

    /**
     * EventBus Unregister
     * onDestroy 에서 호출
     * 주의사항 : super.onDestroy() 전에 호출
     */
    public void unregisterEventBus(Object object){
        OrderEventProvider.getInstance().unregister(object);
    }

    /**
     * Socket Connecting data interaction Interface
     */
    public void setSocketInterface(SocketInterface socketInterface) {
        this.socketInterface = socketInterface;
    }
}
