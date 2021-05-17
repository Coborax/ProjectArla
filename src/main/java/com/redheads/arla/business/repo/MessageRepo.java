package com.redheads.arla.business.repo;

import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.persistence.DashboardMessageDataAccess;

import java.time.LocalTime;

public class MessageRepo extends SimpleRepo<DashboardMessage> {

    public MessageRepo() {
        setDataAccess(new DashboardMessageDataAccess());
    }

    public DashboardMessage getCurrentMessage() {
        LocalTime now = LocalTime.now();
        for (DashboardMessage m : getEntities()) {
            if (m.getStart().isBefore(now) && m.getEnd().isAfter(now)) {
                return m;
            }
        }
        return null;
    }

}