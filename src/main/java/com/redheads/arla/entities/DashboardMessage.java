package com.redheads.arla.entities;

public class DashboardMessage extends Entity {

    private String msg;
    private MessageType type;

    public DashboardMessage(String msg, MessageType type) {
        this.msg = msg;
        this.type = type;
    }
}
