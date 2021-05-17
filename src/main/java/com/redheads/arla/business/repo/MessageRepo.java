package com.redheads.arla.business.repo;

import com.redheads.arla.entities.DashboardMessage;
import com.redheads.arla.persistence.DashboardMessageDataAccess;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo extends SimpleRepo<DashboardMessage> {

    public MessageRepo() throws DataAccessError {
        setDataAccess(new DashboardMessageDataAccess());
        getEntities().addAll(getDataAccess().readAll());
    }

    public List<DashboardMessage> getMessagesWithConfigID(int configID) {
        List<DashboardMessage> result = new ArrayList<>();
        for (DashboardMessage m : getEntities()) {
            if (m.getConfigID() == configID) {
                result.add(m);
            }
        }
        return result;
    }

    public DashboardMessage getCurrentMessage(int configID) {
        LocalTime now = LocalTime.now();
        for (DashboardMessage m : getEntities()) {
            if (m.getConfigID() == configID && m.getStart().isBefore(now) && m.getEnd().isAfter(now)) {
                return m;
            }
        }
        return null;
    }

}