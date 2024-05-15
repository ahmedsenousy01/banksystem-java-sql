package core;

import java.sql.SQLException;
import java.util.Scanner;

import controllers.UserController;
import core.database.Database;
import models.User;

public class App {
    private static final Scanner cin = new Scanner(System.in);

    public static void startApp() throws SQLException {
        Database.initDbConnection();
        String email, password;
        System.out.println("\t\t\t\t\t\tLogin:");

        System.out.println("Enter your email: ");
        email = cin.next();
        System.out.println("Enter your password: ");
        password = cin.next();

        User currentUser = UserController.login(email, password);
        if (currentUser != null) {
            if (UserController.router(currentUser)) {
                startApp();
            }
        }
    }
}
