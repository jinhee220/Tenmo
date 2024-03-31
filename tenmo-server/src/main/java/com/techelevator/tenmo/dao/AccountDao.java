package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface AccountDao {

    public Account getAccountByUserId(int userId);

    // update Account
    public void updateAccount(Account account);

}
