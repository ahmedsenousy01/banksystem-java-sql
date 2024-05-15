package models;

import java.math.BigDecimal;

import models.enums.UserRole;

public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private BigDecimal salary;

    public User() {
    }

    public User(Integer id, String name, String email, String password, UserRole role, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.salary = salary;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public UserRole getRole() {
        return this.role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public boolean setId(Integer id) {
        this.id = id;
        return true;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public boolean setEmail(String email) {
        this.email = email;
        return true;
    }

    public boolean setPassword(String password) {
        this.password = password;
        return true;
    }

    public boolean setRole(UserRole role) {
        this.role = role;
        return true;
    }

    public boolean setSalary(BigDecimal salary) {
        this.salary = salary;
        return true;
    }
}
