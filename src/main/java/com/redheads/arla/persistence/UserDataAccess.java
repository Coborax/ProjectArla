package com.redheads.arla.persistence;

import com.redheads.arla.entities.User;
import com.redheads.arla.persistence.database.DBConnector;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataAccess implements IDataAccess<User> {

    //SQL Queries
    private final String CREATE_SQL = "INSERT INTO Users (Username, Password, IsAdmin, ConfigID) VALUES (?, ?, ?, ?);";
    private final String DELETE_SQL = "DELETE FROM Users WHERE ID=?;";
    private final String SELECT_ALL_SQL = "SELECT * FROM Users;";
    private final String UPDATE_SQL = "UPDATE Users SET Username = ?, Password = ?, IsAdmin = ?, ConfigID = ? WHERE ID = ?;";

    private DBConnector dbConnector = new DBConnector();

    @Override
    public void create(User toCreate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toCreate.getUsername());
            statement.setString(2, toCreate.getPassword());
            statement.setBoolean(3, toCreate.isAdmin());
            statement.setInt(4, toCreate.getConfigID());

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                toCreate.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessError("Could not create user: " + toCreate + " in database", e);
        }
    }

    @Override
    public List<User> readAll() throws DataAccessError {
        List<User> users = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection()) {
            Statement statement = conn.createStatement();
            if (statement.execute(SELECT_ALL_SQL)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password");
                    boolean isAdmin = resultSet.getBoolean("IsAdmin");
                    int configID = resultSet.getInt("ConfigID");

                    User user = new User(username, password, isAdmin, configID);
                    user.setId(id);

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessError("Could not select all users from database", e);
        }
        return users;
    }

    @Override
    public User read(int id) {
        return null;
    }

    @Override
    public void updateAll(List<User> toUpdate) {

    }

    @Override
    public void update(User toUpdate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
            statement.setString(1, toUpdate.getUsername());
            statement.setString(2, toUpdate.getPassword());
            statement.setBoolean(3, toUpdate.isAdmin());
            statement.setInt(4, toUpdate.getConfigID());
            statement.setInt(5, toUpdate.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not update user: " + toUpdate + " in database", e);
        }
    }

    @Override
    public void delete(User toDelete) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
            statement.setInt(1, toDelete.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not delete user: " + toDelete + " in database", e);
        }
    }
}
