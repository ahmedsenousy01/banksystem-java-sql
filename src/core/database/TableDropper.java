package core.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableDropper {
    public static void main(String[] args) {
        dropTables();
    }

    public static void dropTables() {
        Database.initDbConnection();
        try (Connection connection = Database.connection) {
            Statement statement = connection.createStatement();

            // Drop tables if they exist
            statement.executeUpdate("DROP TABLE IF EXISTS transactions");
            statement.executeUpdate("DROP TABLE IF EXISTS accounts");
            statement.executeUpdate("DROP TABLE IF EXISTS users");

            System.out.println("Tables dropped successfully.");

        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
        }
    }
}
