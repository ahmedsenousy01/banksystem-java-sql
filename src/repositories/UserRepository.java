package repositories;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import core.database.Database;
import models.User;
import models.enums.UserRole;

public class UserRepository {

    public static List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String getUsersSQL = "SELECT * FROM users"; // Retrieve all columns
        try (PreparedStatement statement = Database.connection.prepareStatement(getUsersSQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                UserRole role = UserRole.valueOf(resultSet.getString("role").toUpperCase());
                User user = new User(id, name, email, password, role, salary);
                users.add(user);
            }
        }
        return users;
    }

    public static User getUserById(int id) throws SQLException {
        User u = null;
        String getUserByIdSQL = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = Database.connection.prepareStatement(getUserByIdSQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                UserRole role = UserRole.valueOf(resultSet.getString("role").toUpperCase());
                u = new User(id, name, email, password, role, salary);
            }
        }
        return u;
    }

    public static User getUserByEmail(String email) throws SQLException {
        User u = null;
        String getUserByEmailSQL = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = Database.connection.prepareStatement(getUserByEmailSQL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                UserRole role = UserRole.valueOf(resultSet.getString("role").toUpperCase());
                u = new User(id, name, email, password, role, salary);
            }
        }
        return u;
    }

    public static void createUser(User u) throws SQLException {
        String createUserSQL = "INSERT INTO users (name, email, password, role, salary) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = Database.connection.prepareStatement(createUserSQL)) {
            statement.setString(1, u.getName());
            statement.setString(2, u.getEmail());
            statement.setString(3, u.getPassword());
            statement.setString(4, u.getRole().toString()); // Set the enum directly as a string
            if (u.getSalary() == null) {
                statement.setNull(5, Types.DECIMAL);
            } else {
                statement.setBigDecimal(5, u.getSalary());
            }
            statement.executeUpdate();
            System.out.println("User created successfully!");
        }
    }

    public static void updateUser(User u) throws SQLException {
        String updateUserSQL = "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = Database.connection.prepareStatement(updateUserSQL)) {
            statement.setString(1, u.getName());
            statement.setString(2, u.getEmail());
            statement.setString(3, u.getPassword());
            statement.setString(4, u.getRole().name()); // Assuming role is an enum
            statement.setInt(5, u.getId());
            statement.executeUpdate();
            System.out.println("User updated successfully!");
        }
    }

    public static void deleteUser(int id) throws SQLException {
        String deleteUserSQL = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = Database.connection.prepareStatement(deleteUserSQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("User deleted successfully!");
        }
    }
}
