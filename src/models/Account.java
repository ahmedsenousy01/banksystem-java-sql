package models;

import java.math.BigDecimal;

public class Account {
    private Integer id;
    private BigDecimal balance;
    private Integer userId;

    public Account() {
    }

    public Account(Integer id, BigDecimal balance, Integer userId) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }

    public Integer getId() {
        return this.id;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Integer getUserId() {
        return this.userId;
    }


    public boolean setId(Integer id) {
        this.id = id;
        return true;
    }

    public boolean setBalance(BigDecimal balance) {
        this.balance = balance;
        return true;
    }

    public boolean setUserId(Integer userId) {
        this.userId = userId;
        return true;
    }
}
