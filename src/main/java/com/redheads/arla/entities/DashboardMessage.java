package com.redheads.arla.entities;

import java.time.LocalTime;

public class DashboardMessage extends Entity {

    private int configID;

    private String msg;
    private MessageType type;
    private LocalTime start;
    private LocalTime end;

    public DashboardMessage(int configID, String msg, MessageType type, LocalTime start, LocalTime end) {
        this.configID = configID;
        this.msg = msg;
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
