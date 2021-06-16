package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    static Connection connection;

    public static void connect(){
        String url = "jdbc:sqlite:" + Main.dbName;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to a database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(){
        try {
            connection.close();
            System.out.println("Connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
