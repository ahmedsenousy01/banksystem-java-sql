package core.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import core.Config;

public class Database {
    public static Connection connection;

    public static void initDbConnection() {
        try {
            Database.connection = DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASSWORD);
            if (!checkIfTablesExist(connection)) {
                createTables(connection);
            }
        } catch (SQLException e) {
            System.out.println("database connection error!");
            e.printStackTrace();
        }
    }

    private static boolean checkIfTablesExist(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();

            // Check if "users" table exists
            ResultSet usersTable = metaData.getTables(null, null, "users", null);
            boolean usersExist = usersTable.next();

            // Check if "accounts" table exists
            ResultSet accountsTable = metaData.getTables(null, null, "accounts", null);
            boolean accountsExist = accountsTable.next();

            // Check if "accounts" table exists
            ResultSet transactionsTable = metaData.getTables(null, null, "transactions", null);
            boolean transactionsExist = transactionsTable.next();

            // Return true if all tables exist, false otherwise
            return usersExist && accountsExist && transactionsExist;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void createTables(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            // Create users table
            String createUserTableSQL = """
                    CREATE TABLE users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        role ENUM('ADMIN', 'CLIENT', 'EMPLOYEE') NOT NULL,
                        salary DECIMAL(10, 2)
                    )
                    """;
            statement.executeUpdate(createUserTableSQL);

            // Create accounts table
            String createAccountsTableSQL = """
                    CREATE TABLE accounts (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        balance DECIMAL(10, 2) NOT NULL,
                        userId INT,
                        FOREIGN KEY (userId) REFERENCES users(id)
                    )
                    """;
            statement.executeUpdate(createAccountsTableSQL);

            // Create transactions table
            String createTransactionsTableSQL = """
                    CREATE TABLE transactions (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        senderAccountId INT,
                        recipientAccountId INT,
                        amount DECIMAL(10, 2) NOT NULL,
                        transactionType ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
                        transactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (senderAccountId) REFERENCES accounts(id),
                        FOREIGN KEY (recipientAccountId) REFERENCES accounts(id)
                    )
                    """;
            statement.executeUpdate(createTransactionsTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
