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

    /**
     * Fetches a list of dashboard messages from a given config
     * @param configID The id of the config to fetch messages from
     * @return A list of dashboard messages
     */
    public List<DashboardMessage> getMessagesWithConfigID(int configID) {
        List<DashboardMessage> result = new ArrayList<>();
        for (DashboardMessage m : getEntities()) {
            if (m.getConfigID() == configID) {
                result.add(m);
            }
        }
        return result;
    }

    /**
     * Fetches a dashboard message that is suppose to be displayed now
     * @param configID The config id to match
     * @return The dashboard message matching the current time and config id
     */
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