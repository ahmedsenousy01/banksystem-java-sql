package core.database;

import models.*;
import models.enums.*;
import repositories.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DatabaseSeeder {

    public static void seedDatabase() {
        try {
            Database.initDbConnection();

            // Seed users to get their IDs
            User client = new User(null, "Client User", "client@example.com", "client123", UserRole.CLIENT, null);
            User employee = new User(null, "Employee User", "employee@example.com", "employee123", UserRole.EMPLOYEE,
                    new BigDecimal(5000));
            User admin = new User(null, "Admin User", "admin@example.com", "admin123", UserRole.ADMIN,
                    new BigDecimal(10000));

            UserRepository.createUser(client);
            UserRepository.createUser(employee);
            UserRepository.createUser(admin);

            // Retrieve the users to get their auto-generated IDs
            List<User> users = UserRepository.getUsers();

            // Proceed with seeding accounts and transactions using the retrieved IDs
            for (User user : users) {
                // Create two accounts for each user
                AccountRepository.createAccount(user.getId());
                AccountRepository.createAccount(user.getId());

                List<Account> accounts = AccountRepository.getUserAccounts(user.getId());

                // Perform a deposit, withdrawal, and transfer for each user
                AccountRepository.deposit(accounts.get(0).getId(), new BigDecimal(100.00));
                TransactionRepository.createTransaction(accounts.get(0).getId(), null, new BigDecimal(100.00),
                        TransactionType.DEPOSIT);

                AccountRepository.withdraw(accounts.get(0).getId(), new BigDecimal(50.00));
                TransactionRepository.createTransaction(accounts.get(0).getId(), null, new BigDecimal(50.00),
                        TransactionType.WITHDRAWAL);

                // Assuming user 1 transfers to user 2's account
                AccountRepository.transfer(accounts.get(0).getId(), accounts.get(1).getId(), new BigDecimal(30.00));
                TransactionRepository.createTransaction(accounts.get(0).getId(), accounts.get(1).getId(),
                        new BigDecimal(30.00),
                        TransactionType.TRANSFER);
            }

            System.out.println("Database seeding completed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        seedDatabase();
    }
}
