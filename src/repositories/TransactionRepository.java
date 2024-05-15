package repositories;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import core.database.Database;
import models.Transaction;
import models.enums.TransactionType;

public class TransactionRepository {
    public static void createTransaction(int senderAccountId, Integer recipientAccountId, BigDecimal amount,
            TransactionType transactionType) {
        try {
            String createTransactionSql = """
                        INSERT INTO transactions (senderAccountId, recipientAccountId, amount, transactionType)
                        VALUES (?, ?, ?, ?);
                    """;
            PreparedStatement statement = Database.connection.prepareStatement(createTransactionSql);
            statement.setInt(1, senderAccountId);
            if (recipientAccountId != null) {
                statement.setInt(2, recipientAccountId);
            } else {
                statement.setNull(2, Types.INTEGER); // Set recipientAccountId to NULL in the SQL query
            }
            statement.setBigDecimal(3, amount);
            statement.setString(4, transactionType.name().toUpperCase()); // Convert enum to string
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getUserTransactions(int userId) {
        try {
            String getUserTransactionsSql = """
                        SELECT *
                        FROM transactions t
                        INNER JOIN accounts a ON t.senderAccountId = a.id OR t.recipientAccountId = a.id
                        WHERE a.userId = ?;
                    """;
            List<Transaction> transactions = new ArrayList<>();

            PreparedStatement statement = Database.connection.prepareStatement(getUserTransactionsSql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("t.id");
                Integer senderAccountId = resultSet.getInt("t.senderAccountId");
                Integer recipientAccountId = resultSet.getInt("t.recipientAccountId");
                BigDecimal amount = resultSet.getBigDecimal("t.amount");
                TransactionType transactionType = TransactionType.valueOf(resultSet.getString("t.transactionType"));
                Timestamp transactionDate = resultSet.getTimestamp("t.transactionDate");

                Transaction transaction = new Transaction(id, senderAccountId, recipientAccountId, amount,
                        transactionType, transactionDate);
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Transaction> getAccountTransactions(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String getTransactionsSQL = "SELECT * FROM transactions WHERE senderAccountId = ? OR recipientAccountId = ?";
        try (PreparedStatement statement = Database.connection.prepareStatement(getTransactionsSQL)) {
            statement.setInt(1, accountId);
            statement.setInt(2, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int senderAccountId = resultSet.getInt("senderAccountId");
                int recipientAccountId = resultSet.getInt("recipientAccountId");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                Timestamp date = resultSet.getTimestamp("transactionDate");
                TransactionType type = TransactionType.valueOf(resultSet.getString("transactionType"));
                Transaction transaction = new Transaction(id, senderAccountId, recipientAccountId, amount, type, date);
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
