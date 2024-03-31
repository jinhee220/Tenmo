package com.techelevator.tenmo.model;

import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;


public class Account {

    // Instance Variables: int userId, double balance
    private int accountId;
    private int userId;
    private BigDecimal balance;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }


    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // public Account(){} // empty constructor
    public Account() { }
    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + accountId +
                ", username='" + userId + '\'' +
                ", balance=" + balance +
                '}';
    }
// Getters & Setters


    // Constructor


    // Additional Methods


}

