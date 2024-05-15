package controllers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import models.Account;
import models.Transaction;
import models.User;
import models.enums.TransactionType;
import models.enums.UserRole;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import repositories.UserRepository;
import views.UserViews;

public class UserController {
    private static final Scanner cin = new Scanner(System.in);

    public static User login(String email, String password) throws SQLException {
        User u = UserRepository.getUserByEmail(email);

        if (u != null && u.getPassword().equals(password))
            return u;

        return null;
    }

    public static boolean router(User currentUser) throws SQLException {
        if (currentUser.getRole().equals(UserRole.CLIENT)) {
            if (clientRouter(currentUser))
                return true;
        } else if (currentUser.getRole().equals(UserRole.EMPLOYEE)) {
            if (employeeRouter(currentUser))
                return true;
        } else if (currentUser.getRole().equals(UserRole.ADMIN)) {
            if (adminRouter(currentUser))
                return true;
        }

        router(currentUser);
        return false;
    }

    private static boolean clientRouter(User currentUser) throws SQLException {
        UserViews.printClientMenu();

        int choice = 0;
        System.out.println("Enter your choice: ");
        choice = cin.nextInt();

        switch (choice) {
            case 1: {
                List<Account> accounts = AccountRepository.getUserAccounts(currentUser.getId());
                System.out.println("-------------------------");
                for (Account account : accounts) {
                    System.out.println("Account Id: " + account.getId());
                    System.out.println("Account Balance: " + account.getBalance());
                    System.out.println("-------------------------");
                }
                break;
            }
            case 2: {
                // Transfer Money
                List<Account> accounts = AccountRepository.getUserAccounts(currentUser.getId());
                System.out.println("-------------------------");
                for (Account account : accounts) {
                    System.out.println("Account Id: " + account.getId());
                    System.out.println("Account Balance: " + account.getBalance());
                    System.out.println("-------------------------");
                }
                System.out.println("Enter sender's account id: ");
                int senderId = cin.nextInt();
                System.out.println("Enter recipient's account id: ");
                int recipientId = cin.nextInt();
                System.out.println("Enter amount to transfer: ");
                BigDecimal amount = cin.nextBigDecimal();

                // Get sender's account
                Account senderAccount = AccountRepository.getAccountById(senderId);

                // Check if sender has enough balance
                if (senderAccount.getBalance().compareTo(amount) >= 0) {

                    // Update accounts in the database
                    AccountRepository.transfer(senderId, recipientId, amount);
                    TransactionRepository.createTransaction(senderId, recipientId, amount, TransactionType.TRANSFER);

                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("mafeesh floos. a8annehalek? mafeesh floos ya 7alolli. mafeesh floos ya 7alawa");
                }
                break;
            }
            case 3: {
                List<Account> accounts = AccountRepository.getUserAccounts(currentUser.getId());
                System.out.println("-------------------------");
                for (Account account : accounts) {
                    System.out.println("Account Id: " + account.getId());
                    System.out.println("Account Balance: " + account.getBalance());
                    System.out.println("-------------------------");
                }
                System.out.println("Enter the account ID to deposit into: ");
                int accountId = cin.nextInt();
                System.out.println("Enter the amount to deposit: ");
                BigDecimal amount = cin.nextBigDecimal();

                Account account = AccountRepository.getAccountById(accountId);
                if (account != null) {
                    AccountRepository.deposit(accountId, amount);
                    TransactionRepository.createTransaction(accountId, null, amount, TransactionType.DEPOSIT);
                    System.out.println("Funds deposited successfully.");
                } else {
                    System.out.println("Invalid account ID.");
                }
                break;
            }
            case 4: {
                List<Account> accounts = AccountRepository.getUserAccounts(currentUser.getId());
                System.out.println("-------------------------");
                for (Account account : accounts) {
                    System.out.println("Account Id: " + account.getId());
                    System.out.println("Account Balance: " + account.getBalance());
                    System.out.println("-------------------------");
                }
                System.out.println("Enter the account ID to withdraw from: ");
                int accountId = cin.nextInt();
                System.out.println("Enter the amount to withdraw: ");
                BigDecimal amount = cin.nextBigDecimal();

                Account account = AccountRepository.getAccountById(accountId);
                if (account != null) {
                    if (account.getBalance().compareTo(amount) >= 0) {
                        AccountRepository.withdraw(accountId, amount);
                        TransactionRepository.createTransaction(accountId, null, amount, TransactionType.WITHDRAWAL);
                        System.out.println("Funds withdrawn successfully.");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                } else {
                    System.out.println("Invalid account ID.");
                }
                break;
            }
            case 5: {
                List<Account> accounts = AccountRepository.getUserAccounts(currentUser.getId());
                System.out.println("-------------------------");
                for (Account account : accounts) {
                    System.out.println("Account Id: " + account.getId());
                    System.out.println("Account Balance: " + account.getBalance());
                    System.out.println("-------------------------");
                }
                System.out.println("Enter the account ID to view transactions: ");
                int accountId = cin.nextInt();

                List<Transaction> transactions = TransactionRepository.getAccountTransactions(accountId);
                System.out.println("Transactions for Account ID " + accountId + ":");
                System.out.println("-------------------------");
                for (Transaction transaction : transactions) {
                    System.out.println("Transaction ID: " + transaction.getId());
                    System.out.println("Transaction Type: " + transaction.getTransactionType());
                    System.out.println("Amount: " + transaction.getAmount());
                    System.out.println("Date: " + transaction.getTransactionDate());
                    System.out.println("-------------------------");
                }
                break;
            }
            case 6: {
                System.out.println("Enter your current password: ");
                String currentPassword = cin.next();

                if (currentUser.getPassword().equals(currentPassword)) {
                    System.out.println("Enter your new password: ");
                    String newPassword = cin.next();

                    currentUser.setPassword(newPassword);
                    UserRepository.updateUser(currentUser);
                    System.out.println("Password changed successfully.");
                } else {
                    System.out.println("Incorrect current password.");
                }
                break;
            }
            case 7: {
                return true;
            }
            default: {
                System.out.println("Invalid choice");
                System.out.println("try again? [y, n]");
                char c = cin.next().charAt(0);
                if (c == 'y' || c == 'Y')
                    return clientRouter(currentUser);
                else
                    return true;
            }
        }
        return false;
    }

    private static boolean employeeRouter(User currentUser) throws SQLException {
        UserViews.printEmployeeMenu();

        int choice = 0;
        System.out.println("Enter your choice: ");
        choice = cin.nextInt();

        switch (choice) {
            case 1: {
                System.out.println("Enter client name: ");
                String name = cin.next();
                System.out.println("Enter client email: ");
                String email = cin.next();
                System.out.println("Enter client password: ");
                String password = cin.next();

                User client = new User(null, name, email, password, UserRole.CLIENT, null);
                UserRepository.createUser(client);

                System.out.println("Client created successfully.");
                break;
            }
            case 2: {
                System.out.println("Enter client ID to update: ");
                int clientId = cin.nextInt();

                User client = UserRepository.getUserById(clientId);
                if (client != null) {
                    System.out.println("Enter new name: ");
                    String newName = cin.next();
                    System.out.println("Enter new email: ");
                    String newEmail = cin.next();

                    client.setName(newName);
                    client.setEmail(newEmail);
                    UserRepository.updateUser(client);

                    System.out.println("Client details updated successfully.");
                } else {
                    System.out.println("Client not found.");
                }
                break;
            }
            case 3: {
                System.out.println("Enter client ID to delete: ");
                int clientId = cin.nextInt();

                User client = UserRepository.getUserById(clientId);
                if (client != null) {
                    UserRepository.deleteUser(clientId);
                    System.out.println("Client deleted successfully.");
                } else {
                    System.out.println("Client not found.");
                }
                break;
            }
            case 4: {
                System.out.println("Enter client ID to view accounts: ");
                int clientId = cin.nextInt();

                List<Account> accounts = AccountRepository.getUserAccounts(clientId);
                if (!accounts.isEmpty()) {
                    System.out.println("Client Accounts:");
                    for (Account account : accounts) {
                        System.out.println("Account Id: " + account.getId());
                        System.out.println("Account Balance: " + account.getBalance());
                        System.out.println("-------------------------");
                    }
                } else {
                    System.out.println("No accounts found for this client.");
                }
                break;
            }
            case 5: {
                System.out.println("Enter client ID to view transactions: ");
                int clientId = cin.nextInt();

                List<Transaction> transactions = TransactionRepository.getUserTransactions(clientId);
                if (!transactions.isEmpty()) {
                    System.out.println("Client Transactions:");
                    for (Transaction transaction : transactions) {
                        System.out.println("Transaction Id: " + transaction.getId());
                        if (!(transaction.getTransactionType() == TransactionType.WITHDRAWAL
                                || transaction.getTransactionType() == TransactionType.DEPOSIT)) {
                            System.out.println("Sender Account Id: " + transaction.getSenderAccountId());
                            System.out.println("Recipient Account Id: " + transaction.getRecipientAccountId());
                        } else {
                            System.out.println("Account Id: " + transaction.getSenderAccountId());
                        }
                        System.out.println("Amount: " + transaction.getAmount());
                        System.out.println("Transaction Type: " + transaction.getTransactionType());
                        System.out.println("Transaction Date: " + transaction.getTransactionDate());
                        System.out.println("-------------------------");
                    }
                } else {
                    System.out.println("No transactions found for this client.");
                }
                break;
            }
            case 6: {
                System.out.println("Enter your current password: ");
                String currentPassword = cin.next();

                if (currentUser.getPassword().equals(currentPassword)) {
                    System.out.println("Enter your new password: ");
                    String newPassword = cin.next();

                    currentUser.setPassword(newPassword);
                    UserRepository.updateUser(currentUser);
                    System.out.println("Password changed successfully.");
                } else {
                    System.out.println("Incorrect current password.");
                }
                break;
            }
            case 7: {
                return true;
            }
            default: {
                System.out.println("Invalid choice");
                System.out.println("try again? [y, n]");
                char c = cin.next().charAt(0);
                if (c == 'y' || c == 'Y')
                    return employeeRouter(currentUser);
                else
                    return true;
            }
        }
        return false;
    }

    private static boolean adminRouter(User currentUser) throws SQLException {
        UserViews.printAdminMenu();

        int choice = 0;
        System.out.println("Enter your choice: ");
        choice = cin.nextInt();

        switch (choice) {
            case 1: {
                System.out.println("Enter client name: ");
                String name = cin.next();
                System.out.println("Enter client email: ");
                String email = cin.next();
                System.out.println("Enter client password: ");
                String password = cin.next();

                User client = new User(null, name, email, password, UserRole.CLIENT, null);
                UserRepository.createUser(client);

                System.out.println("Client created successfully.");
                break;
            }
            case 2: {
                System.out.println("Enter client ID to update: ");
                int clientId = cin.nextInt();

                User client = UserRepository.getUserById(clientId);
                if (client != null) {
                    System.out.println("Enter new name: ");
                    String newName = cin.next();
                    System.out.println("Enter new email: ");
                    String newEmail = cin.next();

                    client.setName(newName);
                    client.setEmail(newEmail);
                    UserRepository.updateUser(client);

                    System.out.println("Client details updated successfully.");
                } else {
                    System.out.println("Client not found.");
                }
                break;
            }
            case 3: {
                System.out.println("Enter client ID to delete: ");
                int clientId = cin.nextInt();

                User client = UserRepository.getUserById(clientId);
                if (client != null) {
                    UserRepository.deleteUser(clientId);
                    System.out.println("Client deleted successfully.");
                } else {
                    System.out.println("Client not found.");
                }
                break;
            }
            case 4: {
                System.out.println("Enter employee name: ");
                String name = cin.next();
                System.out.println("Enter employee email: ");
                String email = cin.next();
                System.out.println("Enter employee password: ");
                String password = cin.next();

                User employee = new User(null, name, email, password, UserRole.EMPLOYEE, null);
                UserRepository.createUser(employee);

                System.out.println("Employee created successfully.");
                break;
            }
            case 5: {
                System.out.println("Enter employee ID to update: ");
                int employeeId = cin.nextInt();

                User employee = UserRepository.getUserById(employeeId);
                if (employee != null) {
                    System.out.println("Enter new name: ");
                    String newName = cin.next();
                    System.out.println("Enter new email: ");
                    String newEmail = cin.next();

                    employee.setName(newName);
                    employee.setEmail(newEmail);
                    UserRepository.updateUser(employee);

                    System.out.println("Employee details updated successfully.");
                } else {
                    System.out.println("Employee not found.");
                }
                break;
            }
            case 6: {
                System.out.println("Enter employee ID to delete: ");
                int employeeId = cin.nextInt();

                User employee = UserRepository.getUserById(employeeId);
                if (employee != null) {
                    UserRepository.deleteUser(employeeId);
                    System.out.println("Employee deleted successfully.");
                } else {
                    System.out.println("Employee not found.");
                }
                break;
            }
            case 7: {
                System.out.println("Enter client ID to view accounts: ");
                int clientId = cin.nextInt();

                List<Account> accounts = AccountRepository.getUserAccounts(clientId);
                if (!accounts.isEmpty()) {
                    System.out.println("Client Accounts:");
                    for (Account account : accounts) {
                        System.out.println("Account Id: " + account.getId());
                        System.out.println("Account Balance: " + account.getBalance());
                        System.out.println("-------------------------");
                    }
                } else {
                    System.out.println("No accounts found for this client.");
                }
                break;
            }
            case 8: {
                System.out.println("Enter client ID to view transactions: ");
                int clientId = cin.nextInt();

                List<Transaction> transactions = TransactionRepository.getUserTransactions(clientId);
                if (!transactions.isEmpty()) {
                    System.out.println("Client Transactions:");
                    for (Transaction transaction : transactions) {
                        System.out.println("Transaction Id: " + transaction.getId());
                        if (!(transaction.getTransactionType() == TransactionType.WITHDRAWAL
                                || transaction.getTransactionType() == TransactionType.DEPOSIT)) {
                            System.out.println("Sender Account Id: " + transaction.getSenderAccountId());
                            System.out.println("Recipient Account Id: " + transaction.getRecipientAccountId());
                        } else {
                            System.out.println("Account Id: " + transaction.getSenderAccountId());
                        }
                        System.out.println("Amount: " + transaction.getAmount());
                        System.out.println("Transaction Type: " + transaction.getTransactionType());
                        System.out.println("Transaction Date: " + transaction.getTransactionDate());
                        System.out.println("-------------------------");
                    }
                } else {
                    System.out.println("No transactions found for this client.");
                }
                break;
            }
            case 9: {
                System.out.println("Enter your current password: ");
                String currentPassword = cin.next();

                if (currentUser.getPassword().equals(currentPassword)) {
                    System.out.println("Enter your new password: ");
                    String newPassword = cin.next();

                    currentUser.setPassword(newPassword);
                    UserRepository.updateUser(currentUser);
                    System.out.println("Password changed successfully.");
                } else {
                    System.out.println("Incorrect current password.");
                }
                break;
            }
            case 10: {
                return true;
            }
            default: {
                System.out.println("Invalid choice");
                System.out.println("try again? [y, n]");
                char c = cin.next().charAt(0);
                if (c == 'y' || c == 'Y')
                    return adminRouter(currentUser);
                else
                    return true;
            }
        }
        return false;
    }
}
