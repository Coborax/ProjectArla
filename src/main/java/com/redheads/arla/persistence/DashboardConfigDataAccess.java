package com.redheads.arla.persistence;

import com.redheads.arla.entities.ContentType;
import com.redheads.arla.entities.DashboardCell;
import com.redheads.arla.entities.DashboardConfig;
import com.redheads.arla.persistence.database.DBConnector;
import com.redheads.arla.util.exceptions.persistence.DataAccessError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardConfigDataAccess implements IDataAccess<DashboardConfig> {

    //Config SQL Queries
    private final String CREATE_SQL = "INSERT INTO DashboardConfigs (Name, RefreshRate) VALUES (?, ?);";
    private final String DELETE_SQL = "DELETE FROM DashboardConfigs WHERE ID=?;";
    private final String SELECT_ALL_SQL = "SELECT * FROM DashboardConfigs;";
    private final String UPDATE_SQL = "UPDATE DashboardConfigs SET Name = ?, RefreshRate = ? WHERE ID = ?;";

    //Cell SQL Queries
    private final String CREATE_CELL_SQL = "INSERT INTO DashboardCells (ConfigID, [Column], [Row], ColumnSpan, RowSpan, ContentPath) VALUES (?, ?, ?, ?, ?, ?);";
    private final String DELETE_CELL_SQL = "DELETE FROM DashboardCells WHERE ConfigID = ? AND [Column] = ? AND [Row] = ?;";
    private final String SELECT_ALL_CELLS_SQL = "SELECT * FROM DashboardCells WHERE ConfigID = ?;";
    private final String UPDATE_CELL_SQL = "UPDATE DashboardCells SET ContentPath = ?, ContentType = ? WHERE ConfigID = ? AND [Column] = ? AND [Row] = ?;";

    private DBConnector dbConnector = new DBConnector();

    @Override
    public void create(DashboardConfig toCreate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, toCreate.getName());
            statement.setInt(2, toCreate.getRefreshRate());

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                toCreate.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessError("Could not create dashboard config: " + toCreate + " in database", e);
        }
    }

    public void createCell(int configID, DashboardCell cell) throws DataAccessError {
        System.out.println("Creating cell");
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_CELL_SQL);
            statement.setInt(1, configID);
            statement.setInt(2, cell.getColumn());
            statement.setInt(3, cell.getRow());
            statement.setInt(4, cell.getColSpan());
            statement.setInt(5, cell.getRowSpan());
            statement.setString(6, cell.getContentPath());

            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not create dashboard cell: " + cell + " in database", e);
        }
    }

    @Override
    public List<DashboardConfig> readAll() throws DataAccessError {
        List<DashboardConfig> users = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection()) {
            Statement statement = conn.createStatement();
            if (statement.execute(SELECT_ALL_SQL)) {
                ResultSet resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Name");
                    int refreshRate = resultSet.getInt("RefreshRate");

                    PreparedStatement cellStatement = conn.prepareStatement(SELECT_ALL_CELLS_SQL);
                    cellStatement.setInt(1, id);

                    DashboardConfig config = new DashboardConfig(name, refreshRate);
                    config.setId(id);

                   if (cellStatement.execute()) {
                       ResultSet cellResult = cellStatement.getResultSet();
                       while (cellResult.next()) {
                           int col = cellResult.getInt("Column");
                           int row = cellResult.getInt("Row");
                           int colSpan = cellResult.getInt("ColumnSpan");
                           int rowSpan = cellResult.getInt("RowSpan");
                           String contentPath = cellResult.getString("ContentPath");
                           ContentType contentType = ContentType.values()[cellResult.getInt("ContentType")];

                           config.getCells().add(new DashboardCell(col, row, colSpan, rowSpan, contentPath, contentType));
                       }
                   }

                    users.add(config);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessError("Could not select all dashboard configs from database", e);
        }
        return users;
    }

    @Override
    public DashboardConfig read(int id) {
        return null;
    }

    @Override
    public void updateAll(List<DashboardConfig> toUpdate) {

    }

    @Override
    public void update(DashboardConfig toUpdate) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
            statement.setString(1, toUpdate.getName());
            statement.setInt(2, toUpdate.getRefreshRate());
            statement.setInt(3, toUpdate.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new DataAccessError("Could not update dashboard config: " + toUpdate + " in database", e);
        }
    }

    public void updateCell(int configID, DashboardCell cell) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement cellStatement = conn.prepareStatement(UPDATE_CELL_SQL);
            cellStatement.setString(1, cell.getContentPath());
            cellStatement.setInt(2, cell.getContentType().ordinal());
            cellStatement.setInt(3, configID);
            cellStatement.setInt(4, cell.getColumn());
            cellStatement.setInt(5, cell.getRow());
            cellStatement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not update dashboard cell: " + cell + " in database", e);
        }
    }

    @Override
    public void delete(DashboardConfig toDelete) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
            statement.setInt(1, toDelete.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not delete dashboard config: " + toDelete + " in database", e);
        }
    }

    public void deleteCell(int configID, DashboardCell cell) throws DataAccessError {
        try (Connection conn = dbConnector.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_CELL_SQL);
            statement.setInt(1, configID);
            statement.setInt(2, cell.getColumn());
            statement.setInt(3, cell.getRow());

            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessError("Could not delete dashboard cell: " + cell + " in database", e);
        }
    }
}
