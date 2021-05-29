/**
 * @author kjell
 */

package com.redheads.arla.persistence.database;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.redheads.arla.util.config.DBConfigReader;

import java.io.IOException;
import java.sql.Connection;

public class DBConnector {
    private DBConfigReader config;
    private SQLServerDataSource dataSource;

    public DBConnector() {
        try {
            config = new DBConfigReader("db.properties");
            dataSource = new SQLServerDataSource();

            dataSource.setServerName(config.getIP());
            dataSource.setDatabaseName(config.getDBName());
            dataSource.setUser(config.getUsername());
            dataSource.setPassword(config.getPassword());
            dataSource.setPortNumber(1433);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Returns a connection to our database
     * @return The database connection object
     * @throws SQLServerException If there is an error connecting
     */
    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }
}
