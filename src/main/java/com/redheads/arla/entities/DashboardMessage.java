package com.redheads.arla.entities;

import java.time.LocalDateTime;

public class DashboardMessage extends Entity {

    private String msg;
    private MessageType type;
    private LocalDateTime start;
    private LocalDateTime end;

    public DashboardMessage(String msg, MessageType type, LocalDateTime start, LocalDateTime end) {
        this.msg = msg;
        this.type = type;
        this.start = start;
        this.end = end;
    }
}
