package repositories;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.database.Database;
import models.Account;

public class AccountRepository {
    public static List<Account> getUserAccounts(int id) {
        try {
            String getUserAccountsSql = """
                        SELECT *
                        FROM accounts
                        WHERE userId = ?;
                    """;
            List<Account> accounts = new ArrayList<>();

            PreparedStatement statement = Database.connection.prepareStatement(getUserAccountsSql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int accountId = resultSet.getInt("id");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                Account account = new Account(accountId, balance, id);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Account getAccountById(int accountId) throws SQLException {
        Account account = null;
        String getAccountByIdSql = """
                    SELECT *
                    FROM accounts
                    WHERE id = ?;
                """;
        try (PreparedStatement statement = Database.connection.prepareStatement(getAccountByIdSql)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                int userId = resultSet.getInt("userId");
                account = new Account(id, balance, userId);
            }
        }
        return account;
    }

    public static void createAccount(int userId) {
        try {
            String createAccountSql = """
                        INSERT INTO accounts (balance, userId)
                        VALUES (?,?);
                    """;
            PreparedStatement statement = Database.connection.prepareStatement(createAccountSql);
            statement.setBigDecimal(1, BigDecimal.ZERO);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAccount(Account account) {
        try {
            String updateAccountSql = """
                        UPDATE accounts
                        SET balance = ?
                        WHERE id = ?;
                    """;
            PreparedStatement statement = Database.connection.prepareStatement(updateAccountSql);
            statement.setBigDecimal(1, account.getBalance());
            statement.setInt(2, account.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deposit(int accountId, BigDecimal amount) {
        try {
            Account account = getAccountById(accountId);
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            updateAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void withdraw(int accountId, BigDecimal amount) {
        try {
            Account account = getAccountById(accountId);
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            updateAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        try {
            Account fromAccount = getAccountById(fromAccountId);
            Account toAccount = getAccountById(toAccountId);

            if (fromAccount.getBalance().compareTo(amount) >= 0) {
                BigDecimal fromNewBalance = fromAccount.getBalance().subtract(amount);
                BigDecimal toNewBalance = toAccount.getBalance().add(amount);

                fromAccount.setBalance(fromNewBalance);
                toAccount.setBalance(toNewBalance);

                updateAccount(fromAccount);
                updateAccount(toAccount);
            } else {
                System.out.println("Insufficient balance for transfer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAccount(int id) {
        try {
            String deleteAccountSQL = "DELETE FROM accounts WHERE id = ?";
            try (PreparedStatement statement = Database.connection.prepareStatement(deleteAccountSQL)) {
                statement.setInt(1, id);
                statement.executeUpdate();
                System.out.println("Account deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
