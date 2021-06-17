package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    public static void create() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + Main.tableName +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, producer TEXT, amount INTEGER)";
        try {
            Statement statement = DB.connection.createStatement();
            statement.execute(sqlQuery);
            System.out.println("Table " + Main.tableName + " created\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Integer insert(String name, String description, String producer, int amount) {
        String sqlQuery = "INSERT INTO " + Main.tableName + "(name, description, producer, amount) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, producer);
            preparedStatement.setInt(4, amount);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted " + id + " " + name + '\n');
                return id;
            } else {
                System.err.println("Can't insert");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void insert(int id, String name, String description, String producer, int amount) {
        String sqlQuery = "INSERT INTO " + Main.tableName + " (id, name, description, producer, amount) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, producer);
            preparedStatement.setInt(5, amount);
            preparedStatement.executeUpdate();
            System.out.println("Inserted " + id + " " + name + '\n');
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectAll() {
        String sqlQuery = "SELECT * FROM " + Main.tableName;

        try {
            Statement statement = DB.connection.createStatement();
            return statement.executeQuery(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet selectByName(String name) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE name = ?";
        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet selectById(int id) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE id = ?";
        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void delete(int id){
        String sqlQuery = "DELETE FROM " + Main.tableName + " WHERE id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Some title has been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(int id, String name, String description, String producer, int amount) {
        String sqlQuery = "UPDATE " + Main.tableName + " SET name = ?, description = ?," +
                " producer = ?, amount = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, producer);
            preparedStatement.setInt(4, amount);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
            System.out.println("Table updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void truncate() {
        String sqlQuery = "DROP TABLE IF EXISTS " + Main.tableName;

        try {
            Statement statement = DB.connection.createStatement();
            statement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
