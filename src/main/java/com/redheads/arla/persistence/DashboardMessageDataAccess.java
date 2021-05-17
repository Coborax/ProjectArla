package com.redheads.arla.persistence;

import com.redheads.arla.entities.*;
import com.redheads.arla.persistence.database.DBConnector;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DashboardMessageDataAccess implements IDataAccess<DashboardMessage> {

    private final String CREATE_SQL = "INSERT INTO DashboardMessages (ConfigID, Message, MessageType, StartDate, EndDate) VALUES (?, ?, ?, ?, ?);";
    private final String DELETE_SQL = "DELETE FROM DashboardMessages WHERE ID=?;";
    private final String SELECT_ALL_SQL = "SELECT * FROM DashboardMessages;";
    private final String UPDATE_SQL = "UPDATE DashboardMessages SET Message = ?, MessageType = ?, StartDate = ?, EndDate = ? WHERE ID = ?;";

    private DBConnector dbConnector = new DBConnector();

    @Override
    public void create(DashboardMessage toCreate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement createStatement = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
            createStatement.setInt(1, toCreate.getConfigID());
            createStatement.setString(2, toCreate.getMsg());
            createStatement.setInt(3, toCreate.getType().ordinal());
            createStatement.setTime(4, Time.valueOf(toCreate.getStart()));
            createStatement.setTime(5, Time.valueOf(toCreate.getEnd()));

            createStatement.execute();

            ResultSet keys = createStatement.getGeneratedKeys();
            if (keys.next()) {
                toCreate.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Could not create Dashboard message: " + toCreate, e);
        }
    }

    @Override
    public List<DashboardMessage> readAll() throws DataAccessError {
        List<DashboardMessage> messages = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection()) {
            Statement statement = conn.createStatement();
            if (statement.execute(SELECT_ALL_SQL)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int ID = resultSet.getInt("ID");
                    int configID = resultSet.getInt("ConfigID");
                    String msg = resultSet.getString("Message");
                    MessageType type = MessageType.values()[resultSet.getInt("MessageType")];
                    LocalTime start = resultSet.getTime("StartDate").toLocalTime();
                    LocalTime end = resultSet.getTime("EndDate").toLocalTime();

                    DashboardMessage dashMsg = new DashboardMessage(configID, msg, type, start, end);
                    dashMsg.setId(ID);

                    messages.add(dashMsg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Could not select all dashboard messages from database", e);
        }
        return messages;
    }

    @Override
    public DashboardMessage read(int id) {
        return null;
    }

    @Override
    public void updateAll(List<DashboardMessage> toUpdate) {

    }

    @Override
    public void update(DashboardMessage toUpdate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
            statement.setString(1, toUpdate.getMsg());
            statement.setInt(2, toUpdate.getType().ordinal());
            statement.setTime(3, Time.valueOf(toUpdate.getStart()));
            statement.setTime(4, Time.valueOf(toUpdate.getEnd()));
            statement.setInt(5, toUpdate.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new DataAccessError("Could not update dashboard message: " + toUpdate + " in database", e);
        }
    }

    @Override
    public void delete(DashboardMessage toDelete) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
            statement.setInt(1, toDelete.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not delete dashboard message: " + toDelete + " in database", e);
        }
    }
}
