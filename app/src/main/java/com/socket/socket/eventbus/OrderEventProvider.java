package com.socket.socket.eventbus;

public class OrderEventProvider {

    private static final CustomBus EVENT_ODDER_BUS = new CustomBus();
    public static CustomBus getInstance() {
        return EVENT_ODDER_BUS;
    }

    private OrderEventProvider() {
    }

}
