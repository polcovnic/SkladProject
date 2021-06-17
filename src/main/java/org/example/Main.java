package org.example;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Main {
    final static String dbName = "database.db";
    final static String tableName = "counter";

    public static void main( String[] args ) throws Exception {
        DB.connect();
        Table.create();
//        Table.update(2, "setString", "Musical instrument", "Yamaha", 15);
//        Table.insert("Guitar", "Stringed musical instrument", "Cort", 18);
        printResultSet("One", Objects.requireNonNull(Table.selectByName("Guitar")));
        printResultSet("Set", Objects.requireNonNull(Table.selectAll()));
        Table.truncate();
        DB.close();
    }

    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()){
                System.out.println(resultSet.getInt("id") + "\t" +
                        resultSet.getString("name") +
                        "\t" + resultSet.getString("description") +
                        "\t" + resultSet.getString("producer") +
                        "\t" + resultSet.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
