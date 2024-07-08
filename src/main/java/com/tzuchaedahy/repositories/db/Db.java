package com.tzuchaedahy.repositories.db;

import com.tzuchaedahy.exceptions.DbException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Db {
    public static Connection conn;

    public static Properties loadProperties() {
        Properties props = new Properties();

        var path = "/home/tzuchaedahy/VSCode/flood-homeless-support/src/main/resources/db.properties";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            props.load(reader);
        } catch (IOException e) {
            throw new DbException("nao foi possivel carregar as variaveis de ambiente.");
        }

        return props;
    }

    public static Connection getConnection() {
        if (conn == null) {
            Properties properties = loadProperties();
            String dburl = properties.getProperty("dburl");

            try {
                conn = DriverManager.getConnection(dburl, properties);
            }
            catch (SQLException e) {
                throw new DbException("nao foi possivel se conectar ao banco de dados." + e.getMessage());
            }
        }

        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException("nao foi possivel se desconectar ao banco de dados.");
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DbException("nao foi possivel fechar o statement.");
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DbException("nao foi possivel fechar o result set.");
            }
        }
    }
}
