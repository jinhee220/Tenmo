package com.techelevator.tenmo.controller;

/*
 Principal is used here to pull an encrypted user information
 */

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller to authenticate users.
 */


@PreAuthorize("permitAll")
@RestController
public class TenmoController {
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final TransferDao transferDao;

    public TenmoController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }


    // ------------------------------------------------------- //
    // ------------------ ACCOUNT CONTROLS ------------------- //
    // ------------------------------------------------------- //

    @RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable  int userId){
        Account account = accountDao.getAccountByUserId(userId);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return account;
        }
    }

    // update account
    @RequestMapping(path = "/accounts", method = RequestMethod.PUT)
    public void update(@Valid @RequestBody Account account) {
        //account.setUserId(userId);
        try {
            accountDao.updateAccount(account);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not updated");
        }
    }


    // ------------------------------------------------------- //
    // ------------------- USER CONTROLS --------------------- //
    // ------------------------------------------------------- //

    //returns list of users
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUsers(Authentication p) {
        List<User> users = new ArrayList<>();
        users = userDao.getUsers();

        if (users == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance Not Found");
        } else {
            return users;
        }
    }

    @RequestMapping(path = "/users/account/{accountId}", method = RequestMethod.GET)
    public User getUserByAccountId(@PathVariable("accountId") int accountId) {
        try {
            User user = userDao.getUserByAccountId(accountId);
            if (user != null) {
                return user;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ------------------------------------------------------- //
    // ----------------- TRANSFER CONTROLS ------------------- //
    // ------------------------------------------------------- //

    //refactored this to

   // @RequestMapping(path = "transfers/user/{userId}", method = RequestMethod.GET)
    @RequestMapping(path = "/transfers/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable int userId) {
        List<Transfer> result = new ArrayList<>();
        try {
            result = transferDao.getTransfersList(userId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }


   // @RequestMapping(path = "transfers/{transferId}", method = RequestMethod.GET)
    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        try {
            Transfer transfer = transferDao.getTransferById(transferId);
            if (transfer != null) {
                return transfer;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer newTransfer) {
        try {
            transferDao.createTransfer(newTransfer);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/transfers/pending/{userId}", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int userId) {
        try {
            return transferDao.getPendingTransfersList(userId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //update transfer
    @RequestMapping(path = "/transfers", method = RequestMethod.PUT)
    public void update(@Valid @RequestBody Transfer updateTransfer) {

        try {
            transferDao.updateTransfer(updateTransfer);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not updated");
        }
    }
}







