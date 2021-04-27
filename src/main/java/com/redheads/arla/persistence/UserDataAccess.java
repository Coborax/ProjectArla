package com.redheads.arla.persistence;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.redheads.arla.entities.User;
import com.redheads.arla.persistence.database.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataAccess implements IDataAccess<User> {

    private final String CREATE_SQL = "INSERT INTO Users (Username, Password, IsAdmin) VALUES (?, ?, ?);";
    private final String DELETE_SQL = "DELETE FROM Users WHERE ID=?;";
    private final String SELECT_ALL_SQL = "SELECT * FROM Users;";
    private final String UPDATE_SQL = "UPDATE Users SET Username = ?, Password = ?, IsAdmin = ? WHERE ID = ?;";

    private DBConnector dbConnector = new DBConnector();

    @Override
    public void create(User toCreate) {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toCreate.getUsername());
            statement.setString(2, toCreate.getPassword());
            statement.setBoolean(3, toCreate.isAdmin());

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                toCreate.setId(keys.getInt(1));
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> readAll() {
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

                    User user = new User(username, password, isAdmin);
                    user.setId(id);

                    users.add(user);
                }
            }
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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
    public void update(User toUpdate) {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
            statement.setString(1, toUpdate.getUsername());
            statement.setString(2, toUpdate.getPassword());
            statement.setBoolean(3, toUpdate.isAdmin());
            statement.setInt(4, toUpdate.getId());
            statement.execute();
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(User toDelete) {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
            statement.setInt(1, toDelete.getId());
            statement.execute();
        } catch (SQLServerException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
